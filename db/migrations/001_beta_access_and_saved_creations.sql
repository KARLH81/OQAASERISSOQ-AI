-- Draft portable PostgreSQL schema. Not applied to a live database.
-- The API must authenticate requests first and access this database only from
-- server-side credentials. Never ship DATABASE_URL or database credentials in
-- the Android/web client.

CREATE TABLE app_accounts (
    account_id uuid PRIMARY KEY,
    auth_subject text NOT NULL UNIQUE,
    created_at timestamptz NOT NULL DEFAULT now(),
    disabled_at timestamptz
);

-- The configured creator identity is always free. Other free accounts receive
-- an explicit grant from the creator. Store a pseudonymous auth subject, not an
-- email address, in app_accounts.
CREATE TABLE free_access_grants (
    account_id uuid PRIMARY KEY REFERENCES app_accounts(account_id) ON DELETE CASCADE,
    granted_by_account_id uuid NOT NULL REFERENCES app_accounts(account_id),
    granted_at timestamptz NOT NULL DEFAULT now(),
    revoked_at timestamptz,
    CHECK (revoked_at IS NULL OR revoked_at >= granted_at)
);

-- Only rows explicitly saved by the user belong here. Generation requests and
-- unsaved results must not be written to this table.
CREATE TABLE saved_creations (
    creation_id uuid PRIMARY KEY,
    account_id uuid NOT NULL REFERENCES app_accounts(account_id) ON DELETE CASCADE,
    tool text NOT NULL CHECK (tool IN (
        'Allanneq', 'Nutserineq', 'Nipilersuut', 'Pitsanngorsaruk', 'Meeqqat Create'
    )),
    title text NOT NULL DEFAULT '',
    content text NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX saved_creations_account_created_idx
    ON saved_creations(account_id, created_at DESC);

-- Daily counters support atomic server-side usage limits. Do not store prompt
-- text in usage or abuse-control records.
CREATE TABLE daily_usage (
    account_id uuid NOT NULL REFERENCES app_accounts(account_id) ON DELETE CASCADE,
    usage_date date NOT NULL,
    request_count integer NOT NULL DEFAULT 0 CHECK (request_count >= 0),
    output_tokens integer NOT NULL DEFAULT 0 CHECK (output_tokens >= 0),
    PRIMARY KEY (account_id, usage_date)
);

-- Production prerequisites before applying this migration:
-- * verified identity tokens and a configured creator subject;
-- * server-side per-account authorization and transactional quota updates;
-- * database encryption, access logs that omit content, and backup/restore test;
-- * tested export and account-deletion flows.
