const mongoose = require('mongoose');

const volunteerSchema = new mongoose.Schema({
    email: {
        required: true,
        type: String,
        unique: true
    },
    name: {
        required: true,
        type: String
    },
    password: {
        required: true,
        type: String
    },
    contact: {
        required: true,
        type: String
    },
    address: {
        required: true,
        type: String
    },
    image: {
        required: true,
        type: String
    }
});

module.exports = mongoose.model('Volunteer', volunteerSchema);
