# Verification

Date: 2026-10-11. Release: 3.0.3-paper26.3.1 (Beta).

- Production compilation: Paper API 26.3.build.159-beta, Java 25.
- Automated suite: 29 tests, zero failures or skipped tests; JUnit 5.13.4 / MockBukkit 4.116.1.
- MockBukkit uses its matching Paper 26.2 API and embedded registry data. It checks plugin logic; it does not establish 26.3 runtime compatibility.
- Real server integration: Paper 26.3 build 169, Java 25.0.4.1. Startup, 16 checks and clean shutdown passed.
- Server checks: 8:1, 1:1, 16:1 conversions in both directions, offsets, cancellation, End portal/gateway exclusion, configured bounds, logical Y height, command mutation and reload persistence.

The server harness dispatches synthetic portal events against real server worlds and a real entity. It does not connect a Minecraft client or physically walk a player/entity through a portal. Actual portal search, generation, arrival safety and interaction with other plugins remain a manual gameplay check. Keep the release Beta until that check passes.

No live Folia or Spigot test was performed. The unverified upstream Folia declaration was removed. The test server runs separately from the user's server. Windows hardware performance counter warnings appeared during startup; plugin checks and shutdown completed.

Run `./gradlew test` for regression checks. Use `verification/run-smoke.ps1` only against a disposable Paper 26.3 server: the harness rewrites its NetherRatio configuration and shuts it down after testing.

Manual release check: test player and item/mob crossings in both directions with new portals at 1:1 and 8:1, then offsets and boundaries. Check End travel, permissions and another plugin cancelling portal events.
