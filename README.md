# HyperOS XiaoAi Suggestion Region Unlocker (XiaoAi Bypasser)
# 澎湃 HyperOS 小愛建議區域限制解鎖模組

[繁體中文](#zh) | [English](#en)

---

<a name="zh"></a>
## 繁體中文

### 簡介
本模組專為 **HyperOS 國際版 / Xiaomi.eu / 官改 ROM** 使用者設計，旨在精準解鎖 **「超級小愛 / 小愛建議」**（Super XiaoAi / XiaoAi Suggestions）的 CTA 隱私授權與區域限制。

本模組採用**進程級物理隔離 + 方法級 Hook**，完全獨立運作，無需執行任何 Shell 指令或修改全局 Settings / System Properties。

---

### 為什麼選擇本模組？（對比全局 resetprop / 通用偽裝模組）

傳統的屬性偽裝模組（如 SpoofMyDevice）或直接改全局 `resetprop ro.miui.build.region CN` 會引發嚴重的系統級連鎖後遺症：
1. **打時間差失敗**：小愛 AI 引擎 (`AiCrEngine`) 在 App 啟動初期（Static 載入階段）即完成區域判定，通用模組因加載滯後而無效。
2. **系統恢復臃腫與雲控復辟**：全局改 CN 會觸發小米雲控（Cloud Control）重新拉取國行靜默安裝清單，並導致 FCM 後台保活白名單失效。
3. **小米帳號與雙開異常**：跨區屬性衝突會導致小米帳號 Token 驗證失敗無法登入，以及應用雙開（XSpace）沙盒路徑錯誤直接閃退。
4. **影響谷歌生態**：可能干擾 Android Auto 車機無線連線及 GMS 服務。

**本模組優點：**
- **零後遺症（純淨隔離）**：僅作用於勾選的小愛相關進程，小米帳號、雙開、雲控、FCM 及 Android Auto 讀取到的依然是 100% 原生系統環境。
- **方法級 Hook**：直接在記憶體中覆寫 `isInternationalDevice` 傳回值，避開 Class 載入時間差。

---

### 生態鏈與前置建議 (ROM & Localization)

1. **EU 本地化與 ROM 移植**：
   - 若你需要自行移植 ROM 或處理 EU/國際版的本地化，推薦使用 [HyperOS-Port-Python (toraidl/HyperOS-Port-Python)](https://github.com/toraidl/HyperOS-Port-Python)。
   - **免打包替代方案**：如果你不想手動重新打包 ROM，可以直接在社群中搜尋並刷入現成的 **「澎湃本地化模組」**（Magisk / KernelSU 模組）。

2. **啟用超級小愛與全域 AI**：
   - 在完成 ROM 本地化或刷入本地化模組後，若想要完整啟用**超級小愛的所有功能**以及**澎湃全域 AI 引擎**，即可搭配**本模組**使用，完美繞過國際版環境下的 CTA 授權鎖定。

3. **擴充至其他中國版 HyperOS 服務**：
   - 本模組的底層 Hook 邏輯具備通用性。若日後需要將解鎖範圍擴充至其他小米國行服務（例如：相冊 AI 編輯、天氣、錄音機轉文字等），**只需直接在 Vector / LSPosed 的作用域（Scope）中勾選對應的應用包名即可**，無需修改代碼。

---

### 預設目標應用（Vector / LSPosed 作用域）
- `com.miui.personalassistant`（智能助理 / 小愛建議）
- `com.miui.voiceassist`（小愛同學）
- `com.xiaomi.aicr`（小米澎湃 AI 引擎）

### 安裝與使用
1. 下載最新編譯的 `app-debug.apk` 並安裝。
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

<a name="en"></a>
## English

### Introduction
A lightweight, process-isolated LSPosed/Xposed module tailored for **HyperOS Global / Xiaomi.eu / Custom ROMs**. It bypasses CTA privacy consent loops and regional restrictions in **XiaoAi Suggestions (Super XiaoAi)** without triggering global system side effects.

100% standalone — no manual shell commands or global system property modifications required.

---

### Why XiaoAi Bypasser over Global `resetprop`?

Modifying global system properties (`resetprop ro.miui.build.region CN`) or using generic spoofing modules causes severe system-level issues on HyperOS:
1. **Timing Defeat**: Xiaomi's AI Engine (`AiCrEngine`) queries device region status during static class initialization—before standard property profiles load.
2. **Cloud Control & Bloatware Restoration**: Forcing `CN` globally triggers Xiaomi's Cloud Control service to pull China-region dynamic bloatware and wipes FCM background whitelists.
3. **Account & Dual App Crashes**: Regional mismatches cause Xiaomi Account OAuth authentication failures and crash Dual Apps (XSpace) due to broken profile mapping.
4. **GMS Interferences**: May break Android Auto wireless projections and Google ecosystem stability.

**Key Advantages:**
- **Zero Side Effects**: Hooks *only* target AI processes. Xiaomi Account, Dual Apps, Cloud Control, FCM, and Android Auto remain 100% untouched.
- **Direct Method Hooking**: Overrides `isInternationalDevice()` return values directly in memory at runtime, immune to static timing checks.

---

### ROM Localization & Ecosystem Guide

1. **EU Localization & ROM Porting**:
   - For ROM porting and localization, check [HyperOS-Port-Python (toraidl/HyperOS-Port-Python)](https://github.com/toraidl/HyperOS-Port-Python).
   - **Alternative**: You can also use pre-made **HyperOS Localization Modules** via Magisk / KernelSU without repackaging ROMs.

2. **Unlocking Super XiaoAi & System-Wide AI**:
   - Combine your localized ROM/module with **XiaoAi Bypasser** to fully unlock **Super XiaoAi features** and **system-wide AI engines**.

3. **Extending to Other CN-Exclusive Services**:
   - To unlock other CN-exclusive Xiaomi system apps (e.g., Gallery AI Editor, Weather, Recorder Transcription), **simply add their package names to the module's Scope in Vector / LSPosed**.

---

### Default Target Scope (Vector / LSPosed)
- `com.miui.personalassistant` (App Vault / XiaoAi Suggestions)
- `com.miui.voiceassist` (XiaoAi Voice)
- `com.xiaomi.aicr` (Xiaomi AiCr Engine)

### Installation & Setup
1. Download and install the latest `app-debug.apk`.
2. Open **Vector / LSPosed Manager** and enable **XiaoAi Bypasser**.
3. Select the target packages in the module scope.
4. Force restart target services via ADB/Termux (or reboot device):
   ```bash
   su
   am force-stop com.xiaomi.aicr
   am force-stop com.miui.personalassistant
   am force-stop com.miui.voiceassist
   ```

---

### Disclaimer
This project is for educational and personal research purposes only. All trademarks belong to Xiaomi Inc.
