# Samsung S25 internal beta checklist

Use the debug APK from GitHub Actions run [43609336458](https://github.com/KARLH81/OQAASERISSOQ-AI/actions/runs/43609336458). Download the `OQAASERISSOQ-AI-debug` artifact ZIP, extract the APK, and install it on the Samsung S25. This is a debug build for private testing, not a public release.

## Before testing

- Use a general test word or harmless sample text; do not enter children's names, school, address, contact details, health information or passwords.
- Confirm the phone has internet access.
- Record the build/run, Android version and date in the notes below.

## Test each tool

| Tool | Safe check | Result |
| --- | --- | --- |
| Allanneq | Ask for a short greeting in clear Kalaallisut. | |
| Nutserineq | Translate a short, non-sensitive sentence between Kalaallisut, Danish and English. | |
| Nipilersuut | Ask for a short, original song idea from a general topic. | |
| Pitsanngorsaruk | Submit a short sample paragraph and check that meaning is preserved. | |
| Meeqqat Create | Use one general word, such as `seqernup`; check the result stays age-appropriate and asks for no personal details. | |

## App behavior

- Confirm the app opens and the text remains readable while the keyboard is open.
- Confirm the submit button disables while a response is loading and becomes available afterwards.
- Copy a response and paste it into a private note; then try the Android share menu without publishing it.
- Use the clear-on-screen control and confirm the input and result are removed only after confirmation.
- Try empty input and a temporarily unavailable network; confirm the app shows a clear error and recovers for another attempt.
- Confirm the web version button opens the OQAASERISSOQ AI website.

## Notes to send back

- Tool and steps that failed:
- Exact error text or screenshot:
- Text layout or keyboard issue:
- Copy/share/clear issue:
- Anything unsafe or unclear in a generated response:
- Overall: pass / needs fixes

This checklist does not request public release or Play Store installation.
