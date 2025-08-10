const mongoose = require('mongoose');

const adminSchema = new mongoose.Schema({
    email: {
        required: true,
        type: String
    },
    name: {
        required: true,
        type: String
    },
    password: {
        required: true,
        type: String
    },
    image: {
        required: true,
        type: String
    },
    address: {
        required: true,
        type: String
    },
    location: {  // ✅ New field
        required: true,
        type: String
    }
});

module.exports = mongoose.model('Admin', adminSchema);
