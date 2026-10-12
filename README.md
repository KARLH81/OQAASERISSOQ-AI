# OQAASERISSOQ AI — Android internal-test app

The Android app provides five Kalaallisut tools: Allanneq, Nutserineq, Nipilersuut, Pitsanngorsaruk and Meeqqat Create. Requests go through the Vercel server endpoint; the OpenAI API key stays on the server and is never shipped in the APK. Results can be copied or shared from Android.

The web version is available at https://oqaaserissoq-ai.vercel.app.

## Build and verify

- JDK 17, Android SDK 35, Gradle 8.11.1.
- `gradle assembleDebug lintDebug --no-daemon` builds the internal-test APK.
- `node --test tests/chat.test.mjs` exercises backend handling with mocked responses and makes no API calls or charges.
- GitHub Actions runs the tests and APK build and stores the debug APK as an artifact.

## Current limits

This is an internal-test foundation, not yet a full Play Store release. It has no account/login, saved creations, server-enforced usage limits, subscription, audio generation, image generation or video generation. Device testing, release signing, privacy disclosures and Play Console checks remain. Kalaallisut outputs need human review.

Language work can proceed with original or explicitly licensed examples and human reviewers. User drafts are not automatically approved training data. Do not add university content without permission.
