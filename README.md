# 📘 Amar Bangla School – Compliance Update Note

## ✅ What I Have Updated in the App

After reviewing Google Play Families and Ad policies carefully, I have made the following changes to ensure full compliance for a child-directed app (Under 13):

### 1️⃣ Ads & Monetization Changes

* Set **AdMob content rating to “G” (General Audience)**.
* Blocked all **Sensitive Categories** from AdMob (both Standard and Restricted categories).
* Enabled **child-directed treatment** in AdMob settings.
* Using **only banner ads**.
* No interstitial ads.
* No rewarded ads.
* No full-screen ads.
* Banner ads are placed at the bottom and do not interrupt learning content.
* Ads remain hidden if they fail to load.
* No aggressive monetization or misleading ad placement.

### 2️⃣ AD_ID & Data Handling

* I am not using Firebase.
* The `AD_ID` permission is removed from the manifest.
* No manual collection of:

  * AAID
  * Device identifiers
  * SIM info
  * MAC / IMEI
  * Location
* The app does not collect personal data from children.

### 3️⃣ External Links Safety (Parental Gate Added)

Since the app is listed for children under 13, I have added a **Parental Gate (Adult Verification)** before:

* Privacy Policy link
* Website link
* Facebook page
* WhatsApp support

Now:

* A math-based parental verification dialog appears.
* Only after correct answer, external link opens.
* If failed, access is denied.

This prevents children from directly accessing external websites.

### 4️⃣ App Content

* Pure educational Bangla alphabet learning content.
* No violence.
* No gambling.
* No adult themes.
* No social chat features.
* No user-generated content.
* No external webview content inside app.

---

# 🛠 What I Have Updated in Play Console

### Target Audience & Content Section

* Selected: **Children (Under 13)**
* Confirmed child-directed.
* No mixed audience selected.

### Data Safety Section

* No personal data collected.
* No location.
* No advertising ID used for profiling.
* No third-party data sharing.

### Content Rating (IARC)

* Accurately filled all questionnaires.
* No misleading answers.

---

# 📌 What Should Be Verified Again in Play Console

To ensure no rejection due to console misconfiguration, the following should be double-checked:

1. Child-directed treatment is enabled.
2. No mixed age group selected accidentally.
3. Data safety answers exactly match actual app behavior.
4. AdMob app is linked correctly.
5. No old release version contains previous configuration.
6. All SDK versions are up to date.

---

# 🎯 Current Situation

This is the second appeal submission.
Previous rejection did not include a clear explanation or evidence of policy violation.
After investigation, no visible policy violation was found in the app.

All reasonable Families Policy adjustments have now been implemented.

If there is any specific technical or policy-related issue still remaining, detailed guidance would be appreciated so that it can be corrected immediately.

