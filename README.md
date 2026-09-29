# HyperOS Localized AI & Services Bypasser
# 澎湃 HyperOS 國行本地化 AI 與系統服務限制解鎖模組

[繁體中文](#zh) | [English](#en)

---

<a name="zh"></a>
## 繁體中文

> ⚠️ **測試說明：** 本模組僅在 **REDMI K100 PRO MAX (HyperOS 3.X)** 上測試證實可行，其他機型/系統版本請自行測試。

### 簡介
本模組專為 **HyperOS 國際版 / Xiaomi.eu / 官改 ROM** 使用者設計，旨在徹底解除**超級小愛、澎湃全域 AI、智能助理（負一屏）等國內本地化功能**在國際版環境下的**區域限制與功能異常**。

本模組採用**進程級物理隔離 + 方法級 Hook**，完全獨立運作，無需執行任何 Shell 指令，也不需要修改全局 Settings 或系統屬性。

---

### 為什麼選擇本模組？（對比全局 resetprop / 通用屬性偽裝）

傳統的通用屬性偽裝方案或直接改全局 `resetprop ro.miui.build.region CN` 會引發嚴重的系統級連鎖後遺症：
1. **無法覆寫方法邏輯**：通用屬性修改僅作用於環境變數，無法直接介入 App 內部 Class 方法層級的區域判定邏輯。
2. **系統恢復臃腫與雲控復辟**：全局改 CN 會觸發小米雲控（Cloud Control）重新拉取國行靜默安裝清單，並導致 FCM 後台保活白名單失效。
3. **小米帳號與雙開異常**：跨區屬性衝突會導致小米帳號 Token 驗證失敗無法登入，以及應用雙開（XSpace）沙盒路徑錯誤直接閃退。
4. **影響谷歌生態**：可能干擾 Android Auto 車機無線連線及 GMS 服務。

**本模組優點：**
- **零後遺症（純淨隔離）**：僅作用於勾選的小愛與 AI 相關進程，小米帳號、雙開、雲控、FCM 及 Android Auto 讀取到的依然是 100% 原生系統環境。
- **方法級 Hook**：直接在記憶體中覆寫區域判定傳回值，精準高效。

---

### 生態鏈與前置建議 (ROM & Localization)

1. **EU 本地化與 ROM 移植**：
   - 若你需要自行移植 ROM 或處理 EU/國際版的本地化，推薦使用 [HyperOS-Port-Python (toraidl/HyperOS-Port-Python)](https://github.com/toraidl/HyperOS-Port-Python)。
   - **免打包替代方案**：如果你不想手動重新打包 ROM，可以直接在社群中搜尋並刷入現成的 **「澎湃本地化模組」**（Magisk / KernelSU 模組）。

2. **完整啟用國行 AI 與本地服務**：
   - 完成 ROM 本地化或刷入本地化模組後，搭配**本模組**使用，即可無痛解除區域限制，正常使用超級小愛全套功能、澎湃全域 AI 與智能助理。

3. **擴充至其他國行 HyperOS 服務**：
   - 本模組的底層 Hook 邏輯具備通用性。若需要解鎖其他小米國行服務（例如：相冊 AI 編輯、天氣、錄音機轉文字等），**只需直接在 Vector / LSPosed 的作用域（Scope）中勾選對應的應用包名即可**，無需修改代碼。

---

### 預設目標應用（Vector / LSPosed 作用域）
- `com.miui.personalassistant`（智能助理 / 負一屏）
- `com.miui.voiceassist`（小愛同學 / 超級小愛）
- `com.xiaomi.aicr`（小米澎湃 AI 引擎）

### 安裝與使用
1. 下載最新編譯的 `XiaoAi-Bypasser.apk` 並安裝。
2. 打開 **Vector (LSPosed) 管理器**，啟用 **XiaoAi Bypasser** 模組。
3. **作用域 (Scope)** 勾選上述 3 個目標應用（或依需求勾選其他小米系統 App）。
4. 在 Termux 或 ADB 執行以下命令強行重啟服務（或直接重啟手機）：
   ```bash
   su
   am force-stop com.xiaomi.aicr
   am force-stop com.miui.personalassistant
   am force-stop com.miui.voiceassist
   ```

---

### 鳴謝 (Acknowledgements)
- 本模組在 **Google Gemini** AI 的協助下開發構建完成。
- 本專案採 **不定期維護** 模式。

---

<a name="en"></a>
## English

> ⚠️ **Notice:** Confirmed working only on **REDMI K100 PRO MAX (HyperOS 3.X)**. Please test on other models at your own discretion.

### Introduction
A lightweight, process-isolated LSPosed/Xposed module designed for **HyperOS Global / Xiaomi.eu / Custom ROMs**. It removes regional restrictions and fixes functionality anomalies for **Super XiaoAi, System-wide HyperOS AI, App Vault (Personal Assistant), and other China-exclusive local features** on Global ROM environments.

100% standalone — no manual shell commands or global system property modifications required.

---

### Why this module over Global `resetprop`?

Modifying global system properties (`resetprop ro.miui.build.region CN`) or using generic property spoofing methods causes severe system-level issues on HyperOS:
1. **Inability to Override Method Logic**: Generic property spoofing only alters environment variables and cannot intercept internal class methods used for region validation.
2. **Cloud Control & Bloatware Restoration**: Forcing `CN` globally triggers Xiaomi's Cloud Control service to pull China-region dynamic bloatware and wipes FCM background whitelists.
3. **Account & Dual App Crashes**: Regional mismatches cause Xiaomi Account authentication failures and crash Dual Apps (XSpace) due to broken profile mapping.
4. **GMS Interferences**: May break Android Auto wireless projections and Google ecosystem stability.

**Key Advantages:**
- **Zero Side Effects**: Hooks *only* target AI/local processes. Xiaomi Account, Dual Apps, Cloud Control, FCM, and Android Auto remain 100% untouched.
- **Direct Method Hooking**: Overrides region detection methods directly in memory at runtime.

---

### ROM Localization & Ecosystem Guide

1. **EU Localization & ROM Porting**:
   - For ROM porting and localization, check [HyperOS-Port-Python (toraidl/HyperOS-Port-Python)](https://github.com/toraidl/HyperOS-Port-Python).
   - **Alternative**: You can also use pre-made **HyperOS Localization Modules** via Magisk / KernelSU without repackaging ROMs.

2. **Unlocking System-Wide AI & Local Services**:
   - Combine your localized ROM/module with this module to fix regional restrictions and use **Super XiaoAi, HyperOS AI, and App Vault** fully.

3. **Extending to Other CN-Exclusive Services**:
   - To unlock other CN-exclusive Xiaomi system apps (e.g., Gallery AI Editor, Weather, Recorder Transcription), **simply add their package names to the module's Scope in Vector / LSPosed**.

---

### Default Target Scope (Vector / LSPosed)
- `com.miui.personalassistant` (App Vault / Personal Assistant)
- `com.miui.voiceassist` (XiaoAi Voice / Super XiaoAi)
- `com.xiaomi.aicr` (Xiaomi AiCr Engine)

### Installation & Setup
1. Download and install `XiaoAi-Bypasser.apk`.
2. Open **Vector / LSPosed Manager** and enable **XiaoAi Bypasser**.
3. Select target packages in the module scope.
4. Force restart target services via ADB/Termux (or reboot device):
   ```bash
   su
   am force-stop com.xiaomi.aicr
   am force-stop com.miui.personalassistant
   am force-stop com.miui.voiceassist
   ```

---

### Acknowledgements
- This module was developed and built with the assistance of **Google Gemini**.
- This project is maintained on an **irregular / best-effort basis**.

---

### Disclaimer
This project is for educational and personal research purposes only. All trademarks belong to Xiaomi Inc.
