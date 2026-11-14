# OQAASERISSOQ AI — completion board

Updated: 14 November 2026

This board covers only the five delivery tracks for OQAASERISSOQ AI. The priority is a safe, testable internal beta. A public Google Play release is not ready yet.

## 1. Android app

**Done:** Five tools are present: Allanneq, Nutserineq, Nipilersuut, Pitsanngorsaruk and Meeqqat Create. The app targets Android API 36, keeps AI provider credentials on the server, disables Android cloud backup, supports copy/share and has a clear-on-screen control.

**Pending:** Install and exercise the current build on Karl's Samsung S25; fix any device-specific problems; finish accessibility and language review for the screens.

## 2. Kalaallisut quality and rights

**Done:** A fail-closed source registry and CI checks are merged to main. External sources remain blocked for training and redistribution unless permissions and human review are documented.

**In progress:** A human-review worksheet now gives the reviewer a place to record the new morphology cases.

**Pending:** A fluent human reviewer must check those cases and record expected outputs, stable IDs, source rights and review dates. The last project summary recorded just one human-reviewed Gold Dataset entry; the live count has not been verified. The recorded 24/24 automated checks cover structural tests, not Kalaallisut accuracy. Do not advertise a verified quality score.

## 3. Backend and deployment

**Done:** The Vercel API calls the configured AI provider from the server, defaults to OpenAI, fails closed for an unknown provider, sets `store: false`, validates the five tools and keeps provider secrets out of the APK. A future-provider boundary and data-layer rules are documented.

**In progress:** A portable PostgreSQL schema is drafted for pseudonymous accounts, creator-issued free-access grants, explicitly saved creations and daily usage counters. It has not been applied to a live database.

**Pending:** Configure durable storage and verified sign-in before promising saved creations or server-enforced usage limits. Add account deletion, retention rules, abuse controls and operational monitoring. Do not add a paid subscription: the creator must be able to use the app free, and the creator decides who else gets free access.

## 4. Google Play readiness

**Done:** Android API 36 targeting, child-mode notice and internal privacy/Data safety drafts have been prepared.

**Pending:** Verify the drafts against final backend behavior and OpenAI retention settings; publish the privacy policy; add an in-app safety report route and a monitored support destination; complete Play Console target-audience and Data safety declarations; prepare release signing and a release-signed AAB. Do not upload a public release before these items are verified.

## 5. Test and beta

**Done:** GitHub Actions builds the debug APK, runs API and language-rights regression tests, and runs Android 16 emulator UI smoke tests. The last recorded emulator run passed 2/2 UI checks.

**Done:** CI run 42 passed for the exact source tree now on main and produced a debug APK artifact.

**Pending:** Test on Samsung S25 using the beta checklist, collect feedback, then prepare a private internal-test track and tester link.

## Shortest safe route

1. Install the CI-built APK on Samsung S25 and record device issues.
2. Complete fluent human review and rights records for language cases.
3. Provision durable Postgres storage and verified account sign-in behind server-side access controls, using the draft schema as a review starting point.
4. Finalize privacy, safety reporting and support details against the deployed backend.
5. Sign the release build and complete the Google Play internal test.
