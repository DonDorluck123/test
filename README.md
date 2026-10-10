# Meine Aufgaben – Android-App

Eine einfache To-Do-Liste für Android.

- Aufgabe eintippen → **Hinzufügen**
- Antippen → als erledigt markieren (durchgestrichen)
- Lange drücken → löschen
- **Erledigte löschen** räumt die Liste auf
- Aufgaben bleiben nach dem Schließen der App gespeichert

## Installieren

Bei jedem Push baut GitHub Actions automatisch die APK und veröffentlicht sie unter
**Releases** (rechts auf der Repo-Startseite).

1. Auf dem Handy die Release-Seite öffnen und `MeineAufgaben.apk` herunterladen.
2. Datei öffnen. Beim ersten Mal fragt Android, ob der Browser/Dateimanager
   „Apps aus unbekannten Quellen installieren“ darf → erlauben.
3. **Installieren** tippen. Fertig.

## Selbst bauen

Mit Android Studio öffnen oder: `gradle assembleRelease` (benötigt Android SDK).
Die APK liegt dann unter `app/build/outputs/apk/release/app-release.apk`.
