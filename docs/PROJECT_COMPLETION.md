# OQAASERISSOQ AI — completion board

Updated: 14 November 2026

This board covers only the five delivery tracks for OQAASERISSOQ AI. The priority is a safe, testable internal beta. A public Google Play release is not ready yet.

## 1. Android app

**Done:** Five tools are present: Allanneq, Nutserineq, Nipilersuut, Pitsanngorsaruk and Meeqqat Create. The app targets Android API 36, keeps the OpenAI key on the server, supports copy/share and has a clear-on-screen control.

**Pending:** Install and exercise the current build on Karl's Samsung S25; fix any device-specific problems; finish accessibility and language review for the screens.

## 2. Kalaallisut quality and rights

**In progress:** A fail-closed source registry and CI checks are on the language-rights branch. External sources remain blocked for training and redistribution unless permissions and human review are documented.

**Pending:** A fluent human reviewer must check the new morphology cases and record expected outputs, stable IDs, source rights and review dates. The last project summary recorded just one human-reviewed Gold Dataset entry; the live count has not been verified. The recorded 24/24 automated checks cover structural tests, not Kalaallisut accuracy. Do not advertise a verified quality score.

## 3. Backend and deployment

**Done:** The Vercel API calls OpenAI from the server, does not store responses by default, validates the five tools and keeps the API key out of the APK.

**Pending:** Choose and configure durable storage and sign-in before promising saved creations or server-enforced usage limits. Add account deletion, retention rules, abuse controls and operational monitoring. Do not add a paid subscription: the creator must be able to use the app free, and the creator decides who else gets free access.

## 4. Google Play readiness

**Done:** Android API 36 targeting, child-mode notice and internal privacy/Data safety drafts have been prepared.

**Pending:** Verify the drafts against final backend behavior and OpenAI retention settings; publish the privacy policy; add an in-app safety report route and a monitored support destination; complete Play Console target-audience and Data safety declarations; prepare release signing and a release-signed AAB. Do not upload a public release before these items are verified.

## 5. Test and beta

**Done:** GitHub Actions builds the debug APK, runs API and language-rights regression tests, and runs Android 16 emulator UI smoke tests. The last recorded emulator run passed 2/2 UI checks.

**Pending:** Confirm the latest merged build in CI, test on Samsung S25, collect beta feedback, then prepare a private internal-test track and tester link.

## Shortest safe route

1. Finish and pass CI for the language-rights gate.
2. Install the resulting APK on Samsung S25 and record device issues.
3. Resolve the backend storage, sign-in and creator-controlled free-access design.
4. Finalize privacy, safety-reporting and support details against the actual backend.
5. Sign the release build and complete the Google Play internal test.
