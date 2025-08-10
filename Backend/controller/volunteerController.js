const express = require('express');
require('dotenv').config();
const Volunteer = require('../model/Volunteer');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

const router = express.Router();

// Home route
router.get('/', function (req, res) {
  res.send('This is Volunteer Home Page....!!!');
});

// ✅ Register without image
router.post('/register', async (req, res) => {
  try {
    const { email, name, password, contact, address } = req.body;

    const existingVolunteer = await Volunteer.findOne({ email });
    if (existingVolunteer) {
      return res.status(400).json({ message: 'Volunteer already exists with this email' });
    }

    const hashedPassword = await bcrypt.hash(password, 10);

    const newVolunteer = new Volunteer({
      email,
      name,
      password: hashedPassword,
      contact,
      address,
    });

    const saved = await newVolunteer.save();
    res.status(201).json({ message: 'Volunteer registered successfully', volunteer: saved });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ✅ Login
router.post('/login', async (req, res) => {
  try {
    const { email, password } = req.body;

    const volunteer = await Volunteer.findOne({ email });
    if (!volunteer) {
      return res.status(400).json({ message: 'Invalid email or password' });
    }

    const isMatch = await bcrypt.compare(password, volunteer.password);
    if (!isMatch) {
      return res.status(400).json({ message: 'Invalid email or password' });
    }

    const token = jwt.sign({ userId: volunteer._id }, process.env.SECRET_KEY, {
      expiresIn: '1d',
    });

    res.json({
      message: `Welcome ${volunteer.name}`,
      token,
      volunteer,
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ✅ Get volunteer by ID
router.get('/get/:id', async (req, res) => {
  try {
    const volunteer = await Volunteer.findById(req.params.id);
    if (!volunteer) return res.status(404).json({ message: "Volunteer not found" });

    res.json(volunteer);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ✅ Get all volunteers
router.get('/getAll', async (req, res) => {
  try {
    const volunteers = await Volunteer.find();
    res.json(volunteers);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ✅ Update
router.patch('/update/:id', async (req, res) => {
  try {
    const updated = await Volunteer.findByIdAndUpdate(req.params.id, req.body, {
      new: true,
    });
    res.json(updated);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

// ✅ Delete
router.delete('/delete/:id', async (req, res) => {
  try {
    const deleted = await Volunteer.findByIdAndDelete(req.params.id);
    res.send(`Volunteer ${deleted.name} deleted successfully`);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

module.exports = router;
