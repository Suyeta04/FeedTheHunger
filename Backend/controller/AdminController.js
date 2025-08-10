const express = require('express');
require('dotenv').config();
const Admin = require('../model/Admin');
const multer = require('../middleware/multer');
const bcrypt = require("bcrypt");
const jwt = require('jsonwebtoken');

const router = express.Router();

router.get('/', function(req, res) {
    res.send("This is Admin Home Page....!!!");
});

// Register
router.post('/register', multer.single('image'), async function(req, res) {
    try {
        const { email, name, password, address, location } = req.body;

        if (!req.file) {
            return res.status(400).json({ error: 'Image is required' });
        }

        const hash = await bcrypt.hash(password, 10);

        const newAdmin = new Admin({
            email,
            name,
            password: hash,
            address,   // ✅ included since it was in req.body
            location,  // ✅ kept location field
            image: req.file.filename,
        });

        const saved = await newAdmin.save();
        res.status(201).json(saved);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});


// Login
router.post('/login', async function (req, res) {
    try {
        const { email, name, password } = req.body;

        if (!email || !password) {
            return res.status(400).json({ message: "Missing credentials", success: false });
        }

        let admin = await Admin.findOne({ email });
        if (!admin) {
            return res.status(400).json({ message: "Incorrect email or password", success: false });
        }

        const isPasswordMatch = await bcrypt.compare(password, admin.password);
        if (!isPasswordMatch) {
            return res.status(400).json({ message: "Incorrect email or password", success: false });
        }

        const tokenData = { adminId: admin._id };
        const token = jwt.sign(tokenData, process.env.JWT_SECRET, { expiresIn: '1d' }); // ✅ updated

        admin = {
            _id: admin._id,
            name: admin.name,
            email: admin.email,
        };

        res.status(200).cookie("token", token, {
            maxAge: 1 * 24 * 60 * 60 * 1000,
            httpOnly: true,
            sameSite: 'strict'
        }).json({
            message: `Welcome back ${admin.name}`,
            admin,
            success: true
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Get admin by ID
router.get('/get/:id', async function (req, res) {
    try {
        const admin = await Admin.findById(req.params.id);
        if (!admin) return res.status(404).json({ message: "Admin not found" });

        const result = {
            ...admin._doc,
            image: `${req.protocol}://${req.get('host')}/uploads/${admin.image}`,
        };

        res.json(result);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// Get all admins
router.get('/getAllAdmins', async function (req, res) {
    try {
        const admins = await Admin.find();
        const adminsWithImageUrl = admins.map(a => ({
            ...a._doc,
            image: `${req.protocol}://${req.get('host')}/uploads/${a.image}`,
        }));
        res.json(adminsWithImageUrl);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Update admin
router.patch('/update/:id', async function (req, res) {
    try {
        const updatedData = req.body;
        const options = { new: true };
        const result = await Admin.findByIdAndUpdate(req.params.id, updatedData, options);
        res.send(result);
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
});

// Delete admin
router.delete('/delete/:id', async function (req, res) {
    try {
        const data = await Admin.findByIdAndDelete(req.params.id);
        res.send(`Admin ${data.name} deleted`);
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
});

// Filter by name
router.get('/filterByName/:name', async function (req, res) {
    try {
        const admins = await Admin.find({ name: { $regex: req.params.name, $options: 'i' } });
        const adminsWithImageUrl = admins.map(a => ({
            ...a._doc,
            image: `${req.protocol}://${req.get('host')}/uploads/${a.image}`,
        }));
        res.json(adminsWithImageUrl);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;
