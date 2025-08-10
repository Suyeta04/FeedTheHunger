const mongoose = require('mongoose');

const volunteerSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
    },
    email: {
        type: String,
        required: true,
    },
    contact: {
        type: String,
        required: true,
    },
    password: {
        type: String,
        required: true,
    },
});

// ✅ Correct export — use schema after it's declared
module.exports = mongoose.models.Volunteer || mongoose.model('Volunteer', volunteerSchema);
