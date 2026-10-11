# NetherRatio 3.0.3-paper26.3.1 — Paper 26.3 port (Beta)

Unofficial compatibility port of ZyanKLee/NetherRatio, originally created by xDxRAx and maintained by ZyanKLee and the NetherRatio Team. Requires Paper 26.3 and Java 25. Preserves configurable ratios, world pairs, offsets and existing translations.

- Target Paper 26.3 API.
- Allow ordinary players to use the coordinate calculator with netherratio.calc.
- Reject non-finite calculator coordinates and use consistent decimal formatting.
- Preserve cancelled portal events.
- Include complete GPLv3 source, build instructions and checksums.

Verification: 29 automated logic tests and 16 synthetic event/command checks on a real Paper 26.3 build 169 server passed. Startup and clean shutdown passed. Physical client portal travel remains untested; this release is Beta. Folia and Spigot support are not certified.

Install only the main JAR in plugins, replacing the older NetherRatio JAR while the server is stopped. Existing configuration is retained; /netherratio set 1 enables global 1:1 conversion unless a world pair has an explicit ratio.

License: GPLv3, preserved from upstream LICENSE.txt (the upstream README's MIT statement is inconsistent). Download the matching full source.zip from this release for complete corresponding source and build scripts. The sources.jar is supplementary.
