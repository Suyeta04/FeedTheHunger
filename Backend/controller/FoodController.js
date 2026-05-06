const express = require('express');
const Food = require('../model/food');
const multer = require('../middleware/multer');
const admin = require('../firebase');

const router = express.Router();

// Upload food
router.post('/upload', multer.single('image'), async (req, res) => {
    try {
        const { food_type, description, address, user_id, userFcmToken } = req.body;

        if (!req.file) {
            return res.status(400).json({ error: 'Image is required' });
        }

        const newFood = new Food({
            food_type,
            description,
            address,
            status: 'Pending',
            image: req.file.filename,
            user_id,
            userFcmToken
        });

        const saved = await newFood.save();

        console.log("Food saved, sending notification...");

        try {
            console.log("Sending notification...");

            const response = await admin.messaging().send({
                topic: "volunteers",
                notification: {
                    title: "New Food Pickup Available 🍱",
                    body: `${food_type} is available at ${address}`
                },
                data: {
                    foodId: saved._id.toString(),
                    status: "Pending"
                }
            });

            console.log("Notification sent:", response);

        } catch (error) {
            console.log("Notification error:", error);
        }

        res.status(201).json({
            status: true,
            message: 'Food uploaded successfully',
            food: saved
        });

    } catch (err) {
        console.error(err);
        res.status(500).json({
            status: false,
            error: err.message
        });
    }
});


// 1. Available Pickups -> only Pending
router.get('/getAvailablePickups', async (req, res) => {
    try {
        const foods = await Food.find({ status: 'Pending' });

        const withImgUrl = foods.map(p => ({
            ...p._doc,
            image: `${req.protocol}://${req.get('host')}/uploads/${p.image}`,
        }));

        res.status(200).json({
            status: true,
            foods: withImgUrl
        });

    } catch (err) {
        res.status(500).json({
            status: false,
            error: err.message
        });
    }
});


// 2. Assigned Delivery -> only Accepted
router.get('/getAssignedDelivery', async (req, res) => {
    try {
        const foods = await Food.find({ status: 'Accepted' });

        const withImgUrl = foods.map(p => ({
            ...p._doc,
            image: `${req.protocol}://${req.get('host')}/uploads/${p.image}`,
        }));

        res.status(200).json({
            status: true,
            foods: withImgUrl
        });

    } catch (err) {
        res.status(500).json({
            status: false,
            error: err.message
        });
    }
});


// 3. Completed Delivery -> only Delivered
router.get('/getCompletedDelivery', async (req, res) => {
    try {
        const foods = await Food.find({ status: 'Delivered' });

        const withImgUrl = foods.map(p => ({
            ...p._doc,
            image: `${req.protocol}://${req.get('host')}/uploads/${p.image}`,
        }));

        res.status(200).json({
            status: true,
            foods: withImgUrl
        });

    } catch (err) {
        res.status(500).json({
            status: false,
            error: err.message
        });
    }
});


// Get all foods
router.get('/getall', async (req, res) => {
    try {
        const foods = await Food.find();

        const withImgUrl = foods.map(p => ({
            ...p._doc,
            image: `${req.protocol}://${req.get('host')}/uploads/${p.image}`,
        }));

        res.status(200).json({
            status: true,
            foods: withImgUrl
        });

    } catch (err) {
        res.status(500).json({
            status: false,
            error: err.message
        });
    }
});


// Update food status
router.post('/updateStatus', async (req, res) => {
    try {
        const { id, status } = req.body;

        if (!id || !status) {
            return res.status(400).json({
                status: false,
                message: 'id and status are required'
            });
        }

        const updatedFood = await Food.findByIdAndUpdate(
            id,
            { status: status },
            { new: true }
        );

        if (!updatedFood) {
            return res.status(404).json({
                status: false,
                message: 'Food not found'
            });
        }

        // SEND NOTIFICATION TO USER AFTER VOLUNTEER ACCEPT
        if (status === "Accepted" && updatedFood.userFcmToken) {
            try {
                await admin.messaging().send({
                    token: updatedFood.userFcmToken,
                    notification: {
                        title: "Food Request Accepted ✅",
                        body: `Your ${updatedFood.food_type} donation request has been accepted by a volunteer.`
                    },
                    data: {
                        foodId: updatedFood._id.toString(),
                        status: "Accepted"
                    }
                });

                console.log("Notification sent to uploaded user");
            } catch (error) {
                console.log("User notification error:", error);
            }
        }

        res.status(200).json({
            status: true,
            message: 'Status updated successfully',
            food: updatedFood
        });

    } catch (error) {
        res.status(500).json({
            status: false,
            message: error.message
        });
    }
});


// Delete food
router.delete('/delete/:id', async (req, res) => {
    try {
        const deleted = await Food.findByIdAndDelete(req.params.id);

        if (!deleted) {
            return res.status(404).json({
                status: false,
                message: 'Food not found'
            });
        }

        res.status(200).json({
            status: true,
            message: `Food with ${deleted.food_type} has been deleted`
        });

    } catch (err) {
        res.status(400).json({
            status: false,
            error: err.message
        });
    }
});

module.exports = router;