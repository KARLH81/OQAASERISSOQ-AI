# Kalaallisut language-resource rights gate

Updated: 10 November 2026

This repository uses a fail-closed registry for external Kalaallisut language resources:

- Registry: `config/language-resource-registry.json`
- Validation: `tests/language-resource-registry.test.mjs`

## Rule

Public availability is not permission. A source is not approved training data unless the exact item has documented provenance, contributor or rights-holder consent, allowed use, withdrawal handling, privacy review and human language review.

Pending, reference-only and integration-testing material must not be copied into the Gold Dataset or used for model training. Automated promotion remains disabled.

## GiellaLT update

On 10 November 2026, `giellalt/lang-kal` added a GPLv3 section 7 app-store exception:

- https://github.com/giellalt/lang-kal/commit/94171339eab695c6eae41f35c97be5516065614b

The exception permits app-store distribution under its stated condition that GPL source remains available through a channel without the store's restrictive terms. The project does not interpret this exception as independent permission for AI/model training, unrestricted redistribution, closed-source reuse or commercial use of derived datasets. Those uses remain blocked until a source-specific rights review is recorded.

The `giellalt/regtest-kal` repository is kept reference-only because a repository-level license has not been confirmed.

## Human-review plan for new morphology changes

The 10 November morphology/disambiguation change is tracked by upstream commit:

- https://github.com/giellalt/lang-kal/commit/6ea77b39c0ca0fbbadaa6d7addc8f63ad6819769

Before integration, a qualified Kalaallisut reviewer must record expected analyses for the affected forms and contexts, including at least:

1. `imeq/imer` and derived `-tar/-lir` readings;
2. `pujoq/pujortar` and derived `-tar/-lir` readings;
3. added `-tuar-innar`, `-nikuu` and `-sima-nir` forms;
4. negative cases showing that unrelated readings are not removed.

Each case needs a stable ID, source sentence, expected output, reviewer identity, review date and rights status. Until then, the upstream changes are evaluation candidates only and must not change a KALAALLIT AI quality claim.

## Current baseline boundary

The last recorded project baseline remains 32/32 automated tests, one approved Gold Dataset entry and six community cases awaiting human review. These numbers are not a live quality claim. The review UI still needs a confirmed live status connection before presenting them as current production state.
