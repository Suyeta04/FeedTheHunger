const express = require('express');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const User = require('../model/User');
require('dotenv').config();

const router = express.Router();

// ✅ Register
router.post('/register', async (req, res) => {
    console.log(10,req.body)
    try {
        const { name, email, password, contact } = req.body;

        // Check for existing user
        const existingUser = await User.findOne({ email });
        if (existingUser) {
            return res.status(400).json({ message: 'User already exists with this email' });
        }

        // Hash the password
        const hashedPassword = await bcrypt.hash(password, 10);

        // Create new user
        const newUser = new User({
            name,
            email,
            password: hashedPassword,
            contact,
        });

        const user = await newUser.save();
        res.status(201).json({ message: 'User registered successfully', user:  user});
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// ✅ Login
router.post('/login', async (req, res) => {
    try {
        const { email, password } = req.body;

        console.log(43, req.body)

        const user = await User.findOne({ email });
        if (!user) return res.status(400).json({ message: 'Invalid credentials' });

        const isMatch = await bcrypt.compare(password, user.password);
        if (!isMatch) return res.status(400).json({ message: 'Invalid credentials' });

        const token = jwt.sign({ userId: user._id }, process.env.JWT_SECRET || 'default_secret', {
            expiresIn: '7d',
        });

        res.json({ token, user });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// ✅ Get All Users
router.get('/getAllUsers', async (req, res) => {
    try {
        const users = await User.find();
        res.json(users);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// ✅ Get User by ID
router.get('/get/:id', async (req, res) => {
    try {
        const user = await User.findById(req.params.id);
        if (!user) return res.status(404).json({ message: 'User not found' });
        res.json(user);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

// ✅ Update User
router.patch('/update/:id', async (req, res) => {
    try {
        const updatedData = req.body;
        const options = { new: true };
        const result = await User.findByIdAndUpdate(req.params.id, updatedData, options);
        res.json(result);
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
});

// ✅ Delete User
router.delete('/delete/:id', async (req, res) => {
    try {
        const user = await User.findByIdAndDelete(req.params.id);
        if (!user) return res.status(404).json({ message: 'User not found' });
        res.json({ message: 'User deleted successfully' });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

module.exports = router;
