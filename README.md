# Firebase Notification Inbox

A native Android application built with **Kotlin**, **Jetpack Compose**, **Material 3**, **Firebase Cloud Messaging (FCM)**, **Room Database**, **Kotlin Coroutines and Flow**, **Navigation Compose**, and **Coil 3**.

The app receives FCM data messages, saves notification history locally, displays Android system notifications, and provides an inbox with unread filtering and a notification details screen.

<p align="center">
  <a href="https://github.com/aryan-0709/FirebaseNotificationInbox/releases/tag/v1.0.0">
    <img src="https://img.shields.io/badge/Download-Release%20v1.0.0-brightgreen?style=for-the-badge&logo=android" alt="Download release v1.0.0">
  </a>
  <a href="https://github.com/aryan-0709/FirebaseNotificationInbox">
    <img src="https://img.shields.io/badge/GitHub-Repository-181717?style=for-the-badge&logo=github" alt="GitHub repository">
  </a>
</p>

## Project Links

| Resource | Link |
|---|---|
| GitHub repository | [aryan-0709/FirebaseNotificationInbox](https://github.com/aryan-0709/FirebaseNotificationInbox) |
| GitHub profile | [aryan-0709](https://github.com/aryan-0709) |
## Download APK

**Latest Version:** v1.0.0.1
[Download Firebase Notification Inbox APK](https://github.com/aryan-0709/FirebaseNotificationInbox/releases/download/v1.0.0.1/app-release.apk)

[View All Releases](https://github.com/aryan-0709/FirebaseNotificationInbox/releases)

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Assignment Requirements](#assignment-requirements)
- [Screenshots](#screenshots)
- [Application Flow](#application-flow)
- [Architecture and Tech Stack](#architecture-and-tech-stack)
- [Project Structure](#project-structure)
- [FCM Data Payload](#fcm-data-payload)
- [Firebase Setup](#firebase-setup)
- [Build and Run](#build-and-run)
- [Download APK](#download-apk)
- [Testing](#testing)
- [Implementation Notes and Limitations](#implementation-notes-and-limitations)
- [Security](#security)
- [License](#license)

---

## Overview

Firebase Notification Inbox maintains a locally persisted history of push notifications received through Firebase Cloud Messaging.

When an FCM data message arrives, the app extracts its fields, saves the notification in Room, and posts an Android system notification when notification permission and device settings allow it. The Compose UI observes the local database and displays the inbox.

Users can view all notifications, filter unread notifications, open a details screen, and have a notification automatically marked as read. Notification history remains available after the application is closed and reopened.

## Features

### Firebase Cloud Messaging
- Receives FCM data messages through `MyFirebaseMessagingService`.
- Processes incoming data messages while the app is foregrounded or backgrounded, subject to Android and FCM delivery behavior.
- Reads the notification ID, title, description, optional banner URL, and timestamp.
- Posts a system notification using the required notification channel.
- Opens the Notification Inbox when the system notification is tapped.

### Persistent Notification Inbox
- Stores notification records locally using Room.
- Displays banner, title, description, timestamp, and read/unread state.
- Preserves notification history after app restart.
- Uses notification ID as the primary key to prevent duplicate rows.

### Inbox Dashboard and Filtering
- Displays total notification count and unread count.
- Filters between all notifications and unread notifications.
- Shows a visual unread indicator.
- Displays empty states when there are no notifications or no unread results.

### Notification Details
- Opens a dedicated details screen when an inbox item is selected.
- Displays banner, title, description, and timestamp.
- Automatically marks the selected notification as read.
- Reflects the updated read state in the inbox.

### Image Loading and Permission Handling
- Loads remote banner images with Coil 3.
- Shows a fallback when a banner URL is missing, invalid, or cannot be loaded.
- Keeps notification text visible even when image loading fails.
- Requests `POST_NOTIFICATIONS` at runtime on Android 13 (API 33) and later.
- Keeps inbox functionality available if notification permission is denied.

## Assignment Requirements

| Requirement | Implementation |
|---|---|
| Receive Firebase push messages | `MyFirebaseMessagingService` handles FCM data messages. |
| Display Android system notifications | `NotificationHelper` builds and posts system notifications. |
| Required channel ID `assignment` | Configured in `NotificationChannelManager`. |
| Required channel name `Assignment` | Configured in `NotificationChannelManager`. |
| Show notification icon, title, and description | Configured in the notification builder. |
| Handle runtime permission where applicable | Manifest permission and Android 13+ runtime permission flow. |
| Read the required payload fields | Reads `id`, `title`, `description`, `bannerImg`, and `timestamp`. |
| Use a unique notification ID | Room entity uses `id` as its primary key. |
| Persist notification history locally | Room database and DAO. |
| Show banner, title, description, timestamp, and read state | Inbox and Details screens. |
| Preserve history after app restart | Persistent Room storage. |
| System notification tap opens the inbox | Notification `PendingIntent` opens `MainActivity`; Inbox is the start destination. |
| Inbox item opens details | Navigation Compose opens the Details screen. |
| Opening an item marks it as read | Details screen triggers the read-state update. |
| Prevent duplicate rows for the same ID | Primary key with `OnConflictStrategy.REPLACE`. |
| Handle missing or invalid images | Coil loading and fallback UI. |
| Handle optional fields and invalid timestamps | Fallback title/description and receipt-time fallback for invalid timestamps. |
| Keep responsibilities separated | Service, notification, data, repository, ViewModel, and UI layers are separated. |

## Screenshots

The screenshots below were captured from the running application and Android device.

### Notification Inbox

Shows the inbox dashboard, notification cards, banner images, and notification content.

![Notification Inbox](docs/screenshots/inbox.png)

### Notification Details

Shows the banner image, title, description, and timestamp for an opened notification.

![Notification Details](docs/screenshots/notification-details.png)

### Android System Notification

Shows an FCM notification posted by FirebaseNotificationInbox in the Android notification shade.

![Android System Notification](docs/screenshots/system-notification.png)

### Notification Permission Prompt

Shows the in-app action presented when notification permission is not enabled.

![Notification Permission Prompt](docs/screenshots/permission-prompt.png)

### Android Notification Settings

Shows the app's notification setting on the device. Notification delivery through the system tray depends on this setting and runtime permission.

![Android Notification Settings](docs/screenshots/notification-settings.png)

### Unread Notifications

Shows the inbox with the Unread filter selected.

![Unread Notifications](docs/screenshots/unread-filter.png)

## Application Flow

### Incoming notification

```text
Firebase Cloud Messaging
          |
          v
MyFirebaseMessagingService
          |
          v
Read and validate data payload
          |
          v
NotificationRepository
          |
          v
NotificationDao
          |
          v
Room Database
          |
          +------------------------------+
          |                              |
          v                              v
NotificationHelper              NotificationViewModel
          |                              |
          v                              v
Android System Notification      Jetpack Compose Inbox
```

### Navigation

```text
Android System Notification
          |
          v
      MainActivity
          |
          v
    Notification Inbox
          |
          | Select notification
          v
  Notification Details
          |
          v
    Mark as Read
          |
          v
   Updated Inbox State
```

## Architecture and Tech Stack

The application follows an MVVM-style UI architecture with a Repository layer and Room as the local persistence source.

| Area | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Design system | Material 3 |
| Architecture | MVVM + Repository Pattern |
| Asynchronous work | Kotlin Coroutines |
| Observable data | Kotlin Flow / StateFlow |
| Local persistence | Room Database |
| Room code generation | KSP |
| Push notifications | Firebase Cloud Messaging |
| Navigation | Navigation Compose |
| Image loading | Coil 3 |
| Build configuration | Gradle Version Catalog |

### Component responsibilities

- **FCM service:** Receives remote messages and extracts payload fields.
- **Notification helper:** Checks notification availability and posts the Android system notification.
- **Notification channel manager:** Creates the required `assignment` channel.
- **DAO:** Defines Room insert, observe, and read-status operations.
- **Repository:** Provides a data access boundary between the DAO and app logic.
- **ViewModel:** Exposes notification data as `StateFlow` and handles UI-related operations.
- **Compose UI:** Displays the inbox, permission prompt, and notification details.

### Local database

The Room `notification` table contains:

| Column | Type | Purpose |
|---|---|---|
| `id` | `TEXT` (Primary Key) | Unique notification identifier |
| `title` | `TEXT` | Notification title |
| `description` | `TEXT` | Notification description |
| `bannerImageURL` | `TEXT`, nullable | Optional banner URL |
| `timestamp` | `INTEGER` | Payload timestamp in milliseconds |
| `receivedAt` | `INTEGER` | Local receipt timestamp in milliseconds |
| `isRead` | `INTEGER` | Read state (`0` = unread, `1` = read) |

A version 1 → version 2 Room migration adds `receivedAt` and populates it for existing records.

## Project Structure

The following reflects the package and files visible in the supplied Android Studio project view. The Android Studio Android view groups source files by package.

```text
FirebaseNotificationInbox/
└── app/
    └── src/
        └── main/
            ├── java/
            │   └── com/example/firenotificationinbox/
            │       ├── data/
            │       │   ├── local/
            │       │   │   ├── NotificationDao.kt
            │       │   │   ├── NotificationDatabase.kt
            │       │   │   ├── NotificationDatabaseMigration.kt
            │       │   │   ├── NotificationDatabaseProvider.kt
            │       │   │   └── NotificationEntity.kt
            │       │   └── repository/
            │       │       └── NotificationRepository.kt
            │       ├── notification/
            │       │   ├── NotificationChannelManager.kt
            │       │   └── NotificationHelper.kt
            │       ├── service/
            │       │   └── MyFirebaseMessagingService.kt
            │       ├── ui/
            │       │   ├── details/
            │       │   ├── inbox/
            │       │   └── theme/
            │       └── MainActivity.kt
            └── res/
```

The `ui/details`, `ui/inbox`, and `ui/theme` packages contain the corresponding Compose screens and UI support files.

## FCM Data Payload

The app expects notification values in the FCM **data payload**.

| Field | Required | Description |
|---|---|---|
| `id` | Yes | Unique notification ID |
| `title` | No | Notification title; fallback text is used if missing |
| `description` | No | Notification description; fallback text is used if missing |
| `bannerImg` | No | Remote banner image URL |
| `timestamp` | No | Timestamp in milliseconds, represented as a string |

### Example data payload

```json
{
  "id": "notification_101",
  "title": "New Notification",
  "description": "This is a sample notification for the assignment.",
  "bannerImg": "https://example.com/banner.jpg",
  "timestamp": "1791873000000"
}
```

Send these values as FCM **data fields**. The app reads them from `RemoteMessage.data`.

The `id` field is required. Messages without an ID are ignored because the ID is the unique key used to store and identify a notification.

If the timestamp is missing, invalid, or non-positive, the app uses the local receipt time.

## Firebase Setup

### 1. Create or select a Firebase project

1. Open the [Firebase Console](https://console.firebase.google.com/).
2. Create a Firebase project or select an existing project.
3. Add an Android app to the Firebase project.
4. Register the exact application ID:

```text
com.example.firenotificationinbox
```

5. Download `app/google-services.json`.

### 2. Add Firebase configuration

Place the file in the app module:

```text
FirebaseNotificationInbox/
└── app/
    └── google-services.json
```

Do not rename the file. This configuration file is intentionally excluded from the public Git repository; each developer must supply their own Firebase configuration.

### 3. Verify Gradle configuration

The project uses the Firebase BoM and Firebase Messaging dependency through the Gradle Version Catalog. The Google Services Gradle plugin is applied to the app module.

### 4. Send a test message

1. Install and launch the app on a device or emulator with Google Play services.
2. Ensure notifications are enabled for the app.
3. In Firebase Console, open the Cloud Messaging area and create a test message using a **data payload**.
4. Include the fields from the example payload above.
5. Send the test message to the test device using the available Firebase Console targeting flow.

The service saves the data locally. System notification display is subject to Android notification permission and device notification settings.

## Build and Run

### Prerequisites

- Android Studio compatible with the project's Android Gradle Plugin.
- JDK 17.
- Android SDK and build tools required by the project.
- Android device or emulator running Android 7.0 (API 24) or later.
- A Firebase project configured for this application ID.

### Clone the repository

```bash
git clone https://github.com/aryan-0709/FirebaseNotificationInbox.git
cd FirebaseNotificationInbox
```

### Configure Firebase

Follow [Firebase Setup](#firebase-setup) and place your `app/google-services.json` inside the `app/` directory.

### Build a debug APK

```bash
./gradlew assembleDebug
```

On Windows:

```bat
gradlew.bat assembleDebug
```

### Build a release APK

```bash
./gradlew assembleRelease
```

On Windows:

```bat
gradlew.bat assembleRelease
```

The APK is normally generated under:

```text
app/build/outputs/apk/
```

The exact subfolder depends on the selected build variant.

## Download APK

The signed release APK is published as a GitHub Release asset.

- **Release page:** [Firebase Notification Inbox v1.0.0](https://github.com/aryan-0709/FirebaseNotificationInbox/releases/tag/v1.0.0)
- **Direct APK:** [Download app-release.apk](https://github.com/aryan-0709/FirebaseNotificationInbox/releases/download/v1.0.0/app-release.apk)

Install the APK on an Android device. Android may ask you to allow installation from the browser or file manager used to open the APK.

## Testing

The following scenarios were manually tested during development:

| Test scenario | Expected behavior | Result |
|---|---|---|
| FCM message while app is foregrounded | Message is saved and system notification is displayed when allowed | Passed |
| FCM message while app is backgrounded | Message is saved and system notification is displayed when allowed | Passed |
| Tap system notification | App opens the Notification Inbox | Passed |
| Open an inbox item | Details screen opens and item is marked as read | Passed |
| Send another message with the same ID | Existing row is replaced; no duplicate row is created | Passed |
| Close and reopen the app | Previously saved notifications remain available | Passed |
| Valid banner URL | Banner image loads | Passed |
| Invalid or unavailable banner URL | Fallback appears; notification text remains visible | Passed |
| Missing banner URL | Fallback appears; remaining content remains visible | Passed |
| Permission already granted | Permission section shows enabled state | Passed |
| Permission not granted | Enable Notifications action is shown | Passed |
| Tap Enable Notifications | Android runtime permission dialog launches on Android 13+ | Passed |
| Permission disabled in system settings | Inbox data can still be saved; system notification is not shown | Passed |
| Release build | Release APK build completes successfully | Passed |

Manual results reflect the development test setup. FCM delivery can also depend on network connectivity, Firebase configuration, device power management, and Android system behavior.

## Implementation Notes and Limitations

- The FCM `id` field is required and is used as the Room primary key.
- `title` and `description` are treated as optional and receive fallback text when absent.
- `bannerImg` is optional. Image loading failure does not block notification content.
- `timestamp` is expected as a millisecond timestamp string. Invalid or non-positive values fall back to local receipt time.
- Notifications with an existing ID use Room's `REPLACE` conflict strategy. A later message with the same ID replaces the existing row.
- Tapping a system notification opens the inbox, as required by the assignment. It does not deep-link to a specific details item.
- The inbox remains available if notification permission is denied; Android system notifications are posted only when permitted.
- The Room database is persistent but **not encrypted**.
- Replacing an existing notification row with the same ID also writes the incoming read state (initially unread); a repeated message can make that item unread again.
- Delete, clear-all, and mark-all-as-read actions are not currently included.
- The permission status shown in the Compose UI is checked when that UI is composed. If permission is changed later in Android Settings while the app remains in the foreground, the displayed status may not refresh until the screen is recomposed or revisited.
- FCM delivery and background execution are subject to Firebase and Android platform behavior.

## Security

- `app/google-services.json` is excluded from Git using `.gitignore`; add your own Firebase configuration when building locally.
- Do not commit FCM registration tokens, signing keys, keystores, passwords, private keys, or other credentials.
- Keep the release keystore and its passwords backed up in a secure location. They are required to sign future updates with the same signing identity.
- Do not place secrets in the README or source control.
- The Room database is not encrypted; avoid storing sensitive personal information in notification payloads.

## License

No license has been specified for this project. Add a `LICENSE` file and update this section if you decide to publish the repository under an open-source license.
