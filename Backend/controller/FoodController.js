const express = require('express');
const Food = require('../model/food');
const multer = require('../middleware/multer');

const router = express.Router();

// Upload food (with image)
router.post('/upload', multer.single('image'), async (req, res) => {
    try {
        const { food_type, description, address, status } = req.body;
        if (!req.file) return res.status(400).json({ error: 'Image is required' });

        const newFood = new Food({
            food_type,
            description,
            address,
            status,
            image: req.file.filename
        });

        const saved = await newFood.save();
        res.status(201).json(saved);
    } catch (err) {
        res.status(500).json({ error: err.message });
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
        res.json(withImgUrl);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Get food by ID
router.get('/get/:id', async (req, res) => {
    try {
        const data = await Food.findById(req.params.id);
        res.json(data);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Update food
router.patch('/update/:id', async (req, res) => {
    try {
        const updated = await Food.findByIdAndUpdate(req.params.id, req.body, { new: true });
        res.send(updated);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});

// Delete food
router.delete('/delete/:id', async (req, res) => {
    try {
        const deleted = await Food.findByIdAndDelete(req.params.id);
        res.send(`Food with ${deleted.food_type} has been deleted`);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});

// Search food by address
router.get('/searchByLocation/:address', async (req, res) => {
    try {
        const results = await Food.find({
            address: { $regex: req.params.address, $options: 'i' }
        });
        res.json(results);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;
