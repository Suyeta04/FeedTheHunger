# 🍽️ FeedTheHunger

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-43853D?style=for-the-badge&logo=node.js&logoColor=white)
![Express.js](https://img.shields.io/badge/Express.js-404D59?style=for-the-badge)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&logo=Firebase&logoColor=white)

**FeedTheHunger** is a comprehensive, social-impact platform designed to bridge the gap between food surplus and food scarcity. It seamlessly connects individuals willing to donate food with active volunteers who can pick up and distribute the food to those in need, all overseen by administrators.

## 🌟 Key Features

The platform is role-based, ensuring dedicated workflows for Users (Donors), Volunteers, and Admins.

### 👤 User (Donor)
- **Authentication**: Secure Login and Registration using JWT.
- **Donate Food**: Upload details of excess food along with images (via Camera/Gallery), food type, and pickup location.
- **Real-time Notifications**: Receive Firebase push notifications when a volunteer accepts the donation request.
- **Track Status**: Monitor the status of uploaded food (Pending, Accepted, Delivered).
- **Manage Profile**: Update profile information and view personal donation history.

### 🤝 Volunteer
- **Authentication**: Dedicated Volunteer Login and Registration.
- **Available Pickups**: View a real-time feed of pending food donations.
- **Accept Requests**: Claim food pickup requests. This action instantly notifies the respective donor.
- **Task Management**: Track assigned and completed deliveries.
- **Push Notifications**: Receive instant alerts when new food is available in the network.

### 🛡️ Administrator
- **Admin Dashboard**: Centralized control panel to oversee platform activities.
- **Manage Users & Volunteers**: View, update, or remove users and volunteers from the system.
- **System Reports**: Access analytical reports on food donations, completed deliveries, and overall impact.

## 🛠️ Technology Stack

### Frontend (Android Application)
- **Language**: Java / XML
- **Minimum SDK**: 24 | **Target SDK**: 35
- **Networking**: Retrofit, Volley, OkHttp (with logging interceptor)
- **Image Loading**: Glide
- **Push Notifications**: Firebase Cloud Messaging (FCM)

### Backend (RESTful API)
- **Runtime**: Node.js
- **Framework**: Express.js
- **Database**: MongoDB (Mongoose ODM)
- **Authentication**: JSON Web Tokens (JWT) & bcryptjs for password hashing
- **File Uploads**: Multer (Local storage for images)
- **Push Notifications**: Firebase Admin SDK

## ⚙️ Architecture & Workflow
1. **Donation Creation**: User uploads food info + image. Backend saves data to MongoDB and uploads the image locally.
2. **Alert Triggered**: Backend triggers a Firebase Cloud Messaging (FCM) topic notification to all subscribed volunteers (`"New Food Pickup Available"`).
3. **Volunteer Acceptance**: A volunteer views the pending request and taps "Accept". The food status updates to `Accepted`.
4. **Donor Notified**: Backend sends a targeted FCM push notification to the specific User's device token informing them that their request has been accepted.
5. **Delivery**: Volunteer completes the pickup and delivery, updating the status to `Delivered`.

## 🚀 Getting Started

### Prerequisites
- Node.js (v16+)
- MongoDB (Local or Atlas)
- Android Studio (Electric Eel or newer)
- Firebase Project (for FCM credentials)

### Backend Setup
1. Navigate to the `Backend` directory:
   ```bash
   cd Backend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Environment Configuration: Create a `.env` file in the root of the `Backend` directory and add the following:
   ```env
   PORT=3000
   DATABASE_URL=your_mongodb_connection_string
   JWT_SECRET=your_secret_key
   ```
4. Add Firebase Service Account: Place your `firebase-service-account.json` file inside the `Backend` directory.
5. Start the server:
   ```bash
   npm start
   ```
   *The server will run on `http://localhost:3000`.*

### Frontend Setup
1. Open Android Studio and select **Open an existing project**.
2. Navigate to the `Frontend` directory and select it.
3. Sync the project with Gradle files.
4. Add `google-services.json` from your Firebase Console into the `Frontend/app` directory.
5. Update the Base URL: Ensure Retrofit/Volley configurations point to your local machine's IP address (e.g., `http://192.168.x.x:3000`) instead of `localhost`.
6. Run the application on an Android Emulator or physical device.

## 📂 Project Structure

```text
FeedTheHunger/
├── Backend/                 # Node.js/Express API
│   ├── controller/          # Route handlers (Food, User, Volunteer, Admin)
│   ├── model/               # Mongoose DB Schemas
│   ├── middleware/          # Multer, Auth middleware
│   ├── uploads/             # Locally saved images
│   ├── index.js             # Entry point
│   ├── firebase.js          # Firebase Admin configuration
│   └── package.json         # Backend dependencies
│
└── Frontend/                # Android Java Application
    ├── app/
    │   ├── src/main/java/com/example/feedthehunger/
    │   │   ├── Admin/       # Admin activities
    │   │   ├── User/        # User activities
    │   │   ├── Volunteer/   # Volunteer activities
    │   │   └── ...          # Core configurations and models
    │   ├── build.gradle     # App-level build configurations
    │   └── ...
    └── build.gradle         # Project-level build configurations
```

## 🔮 Future Enhancements
- **Pet Route Integrations**: Expanding the platform to facilitate pet food donations and pet welfare.
- **Live Location Tracking**: Implementing Google Maps API for real-time tracking of volunteers during pickup and delivery.
- **Cloud Storage**: Migrating from local Multer storage to AWS S3 or Firebase Cloud Storage for enhanced scalability.
