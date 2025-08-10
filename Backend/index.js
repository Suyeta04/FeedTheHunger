require('dotenv').config();

const express = require('express');
const mongoose = require('mongoose');
const path = require('path');
const cors = require('cors');

const mongoString = process.env.DATABASE_URL;

// Connect to MongoDB
mongoose.connect(mongoString, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
});
const database = mongoose.connection;

database.on('error', (error) => console.log(error));
database.once('connected', () => console.log('✅ Database Connected'));

// Express App Setup
const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
// Serve uploaded images
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

app.use(cors({
    origin:'*'
}))

// ======== Root Route ========
app.get('/', function (req, res) {
    res.send("🚀 Food Home Page");
});

// ======== Routes Setup ========
const userRoutes = require('./controller/UserController');
app.use('/users', userRoutes);


const volunteerRoutes = require('./controller/volunteerController');
app.use('/volunteer', volunteerRoutes);

const adminRoutes = require('./controller/AdminController');
app.use('/admin', adminRoutes);

// ✅ Food routes added here
const foodRoutes = require('./controller/FoodController');
app.use('/food', foodRoutes);

// TODO: Add Pet routes when available
// const petRoutes = require('./controller/PetController');
// app.use('/pets', petRoutes);

// Server Start
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`🚀 Server running at http://localhost:${PORT}`);
});
