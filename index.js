require('dotenv').config();

const express = require('express');
const mongoose = require('mongoose');
const mongoString = process.env.DATABASE_URL;
const multer = require('./middleware/multer'); // assuming multer setup is already done

// Connect to MongoDB
mongoose.connect(mongoString);
const database = mongoose.connection;

database.on('error', (error) => console.log(error));
database.once('connected', () => console.log('Database Connected'));

// Express App Setup
const app = express();
app.use(express.json());
app.use('/uploads', express.static('uploads'));

app.get('/', function (req, res) {
    res.send("This is Home Page....!!!");
});

// ======== User Controller Setup ========
const usercontroller = require('./controller/UserController');
app.use('/user', usercontroller);

// ======== Volunteer Controller Setup (No router used inside) ========
const {
    registerVolunteer,
    loginVolunteer,
    getAllVolunteers,
    getVolunteerById,
    updateVolunteer,
    deleteVolunteer,
    filterVolunteersByName
} = require('./controller/volunteerController'); // ✅ all lowercase

// Volunteer routes
app.post('/volunteer/register', multer.single('image'), registerVolunteer);
app.post('/volunteer/login', loginVolunteer);
app.get('/volunteer/getall', getAllVolunteers);
app.get('/volunteer/profile/:id', getVolunteerById);
app.patch('/volunteer/update/:id', updateVolunteer);
app.delete('/volunteer/delete/:id', deleteVolunteer);
app.get('/volunteer/search/:name', filterVolunteersByName);

// Server Listening
app.listen(3000, () => {
    console.log(`Server Started at ${3000}`);
});
