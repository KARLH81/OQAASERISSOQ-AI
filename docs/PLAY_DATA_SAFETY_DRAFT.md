# Google Play Data safety working draft

**Status:** Internal preparation only. This is not a submitted or approved Play Console declaration.  
**Reviewed against code:** 24 October 2026

## Current code-based assessment

| Question | Current evidence | Draft answer |
| --- | --- | --- |
| Does the app collect user content? | User-entered text is transmitted off-device to the app server and OpenAI. | Yes — user content is processed. |
| Is data encrypted in transit? | Android endpoint and provider API use HTTPS. | Yes. |
| Can users request deletion? | No account or saved-creations database exists; no monitored support contact is configured. | Not ready — contact and deletion handling must be defined. |
| Is data sold? | No advertising or sale mechanism appears in the current Android code. | No, based on current code. |
| Is data shared with service providers? | Vercel hosts the endpoint; OpenAI generates responses. | Yes, for app functionality. |
| Are precise location, contacts, photos, microphone, camera or advertising ID collected? | Manifest requests INTERNET only. | No, based on current Android manifest. |
| Are accounts supported? | No login/account implementation. | No. |
| Is user content stored by the app? | No app database or saved-creations backend; OpenAI request sets `store: false`. | No app-controlled persistent storage observed. Provider technical logs require policy review. |
| Is the app child-directed? | It includes Meeqqat Create and child-facing notices. | Release classification must be decided in Play Console. Families-policy review is required. |

## Must be completed before Play Console submission

1. Add a monitored developer support contact.
2. Publish a final privacy-policy URL that matches actual production behavior.
3. Review Vercel and OpenAI retention and subprocessors for the production configuration.
4. Add an in-app method for reporting unsafe AI-generated content.
5. Define how reports are received, reviewed, and resolved.
6. Run moderation and child-safety test cases and retain results.
7. Decide the Play target audience and complete Families-policy checks if children are included.
8. Re-check the final manifest and production dependencies before answering Data safety.
9. Confirm whether any analytics, crash-reporting, login, payment, subscription, or saved-content feature has been added; each changes the declaration.
10. Do not submit this draft as-is without a final production verification.

## Evidence boundaries

This draft describes repository code only. It does not prove provider retention, Play policy approval, signed-release status, Play Console upload, or tester access.
