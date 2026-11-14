# Privacy Policy Draft — OQAASERISSOQ AI

**Status:** Internal review draft. Not yet a published legal policy.  
**Last updated:** 14 November 2026

## What the app does

OQAASERISSOQ AI helps users write, improve, translate, and create short text in Kalaallisut. It also includes a child-oriented creative mode.

## Data processed

When a user submits text, the app sends:

- the text entered by the user;
- the selected tool;
- source and target language choices when translation is used.

The Android app sends this information over the internet to the OQAASERISSOQ AI server hosted by Vercel. The server selects a provider using its `AI_PROVIDER` setting, which defaults to OpenAI in the code. The production environment's actual provider setting has not been verified. The OpenAI adapter forwards the request to OpenAI to generate a response.

Users must not submit passwords, payment details, health records, addresses, contact details, school names, or other sensitive or identifying personal information. Children should use the app with a parent or guardian and must not submit identifying information.

## Storage

The current Android app has no account system and does not save creations in an app database. The only implemented provider is OpenAI and its server request uses `store: false`. The app does not save submitted text or responses in an app database. Infrastructure and provider technical logging and retention must still be checked against the deployed account settings before publication.

## Sharing

Submitted text is processed by:

- Vercel, which hosts the server endpoint;
- OpenAI, the only provider adapter currently implemented in the code.

The app does not sell user data. No advertising SDK is currently included in the Android app.

## Device permissions

The Android app currently requests internet access only. It does not request contacts, location, camera, microphone, photos, or advertising ID permissions.

## Retention and deletion

OQAASERISSOQ AI does not currently maintain user accounts or a saved-creations database. A verified support contact and a complete provider-retention review must be added before this draft is published as the final privacy policy.

## Children

The child-oriented mode is designed to produce age-appropriate creative text. This notice and model instruction are safeguards, but they are not proof that every generated response is suitable. Parent or guardian supervision is required during internal testing. In-app AI-content reporting and moderation testing must be completed before a public child-directed release.

## Security

AI provider credentials remain on the server and are not included in the Android APK. Internet transmission uses HTTPS. Changing the provider requires a fresh retention and privacy review.

## Contact

**Required before publication:** add a monitored support email or contact page controlled by the developer.

## Changes

Material changes to data processing must be reflected in this policy and in the Google Play Data safety declaration before release.
