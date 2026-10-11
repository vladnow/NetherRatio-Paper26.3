# NetherRatio Port

An unofficial Paper 26.3 compatibility port of [NetherRatio](https://github.com/ZyanKLee/NetherRatio), originally created by xDxRAx and maintained by ZyanKLee and the NetherRatio Team.

Set Nether/Overworld coordinate ratios globally or for each world pair. Use 1:1 travel, custom ratios, X/Z offsets, coordinate bounds and a built-in calculator. Existing configuration and upstream English, German, French, Italian and Korean messages are retained.

Requires **Paper 26.3 and Java 25**. Install the main JAR into plugins while the server is stopped, replacing the older NetherRatio JAR. Run /netherratio set 1 for a global 1:1 ratio or edit plugins/NetherRatio/config.yml and run /netherratio reload.

This port targets the new API, corrects calculator permissions and finite coordinate validation, standardizes decimal output and preserves cancelled portal events. It is not an official upstream release.

Beta verification: 29 automated logic tests and 16 synthetic event/command checks on a real Paper 26.3 build 169 server. Physical player portal crossings have not been tested. Portal searches/generation still use server mechanics. Bounds do not verify pregenerated chunks or guarantee a safe landing. Folia and Spigot are not certified.

Licensed under GPLv3. The matching complete source ZIP and build instructions must be offered alongside this binary; upstream attribution and LICENSE.txt are preserved. AI assistance was used for the port changes and publication text.

Port source and matching complete source release: https://github.com/vladnow/NetherRatio-Paper26.3

