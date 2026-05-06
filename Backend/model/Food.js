const mongoose = require('mongoose');

const foodSchema = new mongoose.Schema({
    food_type: {
        required: true,
        type: String
    },
    description: {
        required: true,
        type: String
    },
    upload_date_time: {
        required: true,
        type: Date,
        default: Date.now
    },
    address: {
        required: true,
        type: String
    },
    status: {
        required: true,
        type: String,
        default: "Pending"
    },
    image: {
        required: true,
        type: String
    },
    user_id: {
        type: String
    },
    userFcmToken: {
        type: String
    }
});

module.exports = mongoose.model('Food', foodSchema);