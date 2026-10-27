# Privacy Policy Draft — OQAASERISSOQ AI

**Status:** Internal review draft. Not yet a published legal policy.  
**Last updated:** 24 October 2026

## What the app does

OQAASERISSOQ AI helps users write, improve, translate, and create short text in Kalaallisut. It also includes a child-oriented creative mode.

## Data processed

When a user submits text, the app sends:

- the text entered by the user;
- the selected tool;
- source and target language choices when translation is used.

The Android app sends this information over the internet to the OQAASERISSOQ AI server hosted by Vercel. The server forwards the request to OpenAI to generate a response.

Users must not submit passwords, payment details, health records, addresses, contact details, school names, or other sensitive or identifying personal information. Children should use the app with a parent or guardian and must not submit identifying information.

## Storage

The current Android app has no account system and does not save creations in an app database. The server request to OpenAI uses `store: false`. Infrastructure providers may still process limited technical logs for security, reliability, and abuse prevention under their own terms and policies.

## Sharing

Submitted text is processed by:

- Vercel, which hosts the server endpoint;
- OpenAI, which generates the response.

The app does not sell user data. No advertising SDK is currently included in the Android app.

## Device permissions

The Android app currently requests internet access only. It does not request contacts, location, camera, microphone, photos, or advertising ID permissions.

## Retention and deletion

OQAASERISSOQ AI does not currently maintain user accounts or a saved-creations database. A verified support contact and a complete provider-retention review must be added before this draft is published as the final privacy policy.

## Children

The child-oriented mode is designed to produce age-appropriate creative text. This notice and model instruction are safeguards, but they are not proof that every generated response is suitable. Parent or guardian supervision is required during internal testing. In-app AI-content reporting and moderation testing must be completed before a public child-directed release.

## Security

The OpenAI API key remains on the server and is not included in the Android APK. Internet transmission uses HTTPS.

## Contact

**Required before publication:** add a monitored support email or contact page controlled by the developer.

## Changes

Material changes to data processing must be reflected in this policy and in the Google Play Data safety declaration before release.
