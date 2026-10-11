# NetherRatio Rebased

An unofficial compatibility port of NetherRatio for **Paper 26.3 and Java 25**. Choose a 1:1 Nether/Overworld ratio, keep vanilla 8:1, or configure separate ratios and X/Z offsets for each world pair. Includes a player coordinate calculator and optional coordinate bounds.

## Changes in this port
- Compile against the Paper 26.3 API.
- Fix calculator access for ordinary players using `netherratio.calc`.
- Reject non-finite coordinates and standardize decimal formatting.
- Preserve cancelled player and entity portal events.

## Installation
Stop the server, back up worlds and plugin configuration, and replace the older NetherRatio JAR in `plugins`. Start Paper 26.3 with Java 25. Use `/netherratio set 1` for global 1:1 conversion, or edit `plugins/NetherRatio/config.yml` and run `/netherratio reload`. Explicit per-world ratios override the default.

## Verification and limitations
Beta: 29 automated logic tests and 16 synthetic event/command checks on a real Paper 26.3 build 169 server passed. Actual portal crossings with a Minecraft client remain untested. Server portal search and generation determine the final exit. Coordinate bounds do not check pregenerated chunks or guarantee a safe landing. Spigot and Folia support are not certified.

## License, source and credits
GPLv3. Original work by xDxRAx, maintained by ZyanKLee and the NetherRatio Team. This is an independent port, not an official upstream release.

[Original project](https://github.com/ZyanKLee/NetherRatio) · [Port source and complete matching source releases](https://github.com/vladnow/NetherRatio-Paper26.3)

AI assistance was used for port modifications, verification tooling and publication text. The icon is an abstract illustration, not a gameplay screenshot.
