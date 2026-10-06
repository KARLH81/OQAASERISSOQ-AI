# OQAASERISSOQ AI — internal-test foundation

This branch preserves the Android foundation. Its four buttons open the corresponding existing web tools in the browser. It is an initial launcher, not yet a complete native AI app. It does not depend on an Ilisimatusarfik agreement.

## Build and verify

- JDK 17, Android SDK 35, Gradle 8.11.1.
- `gradle assembleDebug lintDebug` builds the internal-test APK.
- `node --test tests/chat.test.mjs` verifies backend handling with mocked responses, without API charges.
- GitHub Actions installs pinned Gradle instead of calling a missing wrapper.

Backend and web changes need deployment to https://oqaaserissoq-ai.vercel.app before the corrected live functionality is available. OPENAI_API_KEY stays server-side, never in the APK or repository.

## Pending

Device tests, release signing, authentication, server-enforced usage controls, privacy disclosures and Play release checks. This version does not implement audio-song/image/video generation, child mode or subscriptions. Kalaallisut quality still requires human review.

Language work can proceed with original or explicitly licensed examples and human reviewers. User drafts are not automatically approved training data. Do not add university content without permission.
