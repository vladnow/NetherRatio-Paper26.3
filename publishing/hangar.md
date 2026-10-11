# NetherRatio Port

Unofficial Paper 26.3 port of [NetherRatio by xDxRAx and ZyanKLee](https://github.com/ZyanKLee/NetherRatio).

Customize Nether/Overworld coordinate conversion with global or per-world ratios, offsets and optional coordinate bounds. Includes the upstream calculator and five message languages.

Requires Paper 26.3 and Java 25. Replace the old NetherRatio JAR in plugins while the server is stopped. Configure plugins/NetherRatio/config.yml, then /netherratio reload. For global 1:1 conversion use /netherratio set 1. Per-world explicit ratios override the global setting.

This version updates the API target and fixes calculator permissions, finite input validation, decimal formatting and cancellation handling. GPLv3, with upstream author notices and complete matching source available alongside the binary.

Beta: 29 automated logic tests and 16 synthetic event/command checks on a real Paper 26.3 build 169 server passed. Physical portal crossings with a Minecraft client remain untested. Folia/Spigot support is not certified. No safe-landing or pregeneration guarantee is provided by coordinate bounds.

Port source and matching complete source release: https://github.com/vladnow/NetherRatio-Paper26.3

