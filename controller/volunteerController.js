const Volunteer = require('../model/Volunteer');
const bcrypt = require("bcrypt");
const jwt = require('jsonwebtoken');

// Register Volunteer
const registerVolunteer = async (req, res) => {
  try {
    const { email, name, password, contact, address } = req.body;
    if (!req.file) return res.status(400).json({ error: 'Image is required' });

    const hashedPassword = await bcrypt.hash(password, 10);

    const newVolunteer = new Volunteer({
      email,
      name,
      password: hashedPassword,
      contact,
      address,
      image: req.file.filename,
    });

    const saved = await newVolunteer.save();
    res.status(201).json(saved);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// Volunteer Login
const loginVolunteer = async (req, res) => {
  try {
    const { email, password } = req.body;
    if (!email || !password)
      return res.status(400).json({ message: "Missing credentials" });

    let volunteer = await Volunteer.findOne({ email });
    if (!volunteer) return res.status(400).json({ message: "Invalid credentials" });

    const isMatch = await bcrypt.compare(password, volunteer.password);
    if (!isMatch) return res.status(400).json({ message: "Invalid credentials" });

    const tokenData = { userId: volunteer._id };
    const token = jwt.sign(tokenData, process.env.SECRET_KEY, { expiresIn: '1d' });

    const volunteerData = {
      _id: volunteer._id,
      name: volunteer.name,
      email: volunteer.email,
      contact: volunteer.contact,
    };

    res.status(200).cookie("token", token, { maxAge: 1 * 24 * 60 * 60 * 1000, httpOnly: true }).json({
      message: `Welcome back ${volunteer.name}`,
      volunteer: volunteerData,
      success: true
    });

  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// Get All Volunteers
const getAllVolunteers = async (req, res) => {
  try {
    const volunteers = await Volunteer.find();
    const withImageURL = volunteers.map(v => ({
      ...v._doc,
      image: `${req.protocol}://${req.get('host')}/uploads/${v.image}`,
    }));
    res.json(withImageURL);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// Get Volunteer Profile by ID
const getVolunteerById = async (req, res) => {
  try {
    const volunteer = await Volunteer.findById(req.params.id);
    res.json(volunteer);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
};

// Update Volunteer
const updateVolunteer = async (req, res) => {
  try {
    const updated = await Volunteer.findByIdAndUpdate(req.params.id, req.body, { new: true });
    res.json(updated);
  } catch (err) {
    res.status(400).json({ message: err.message });
  }
};

// Delete Volunteer
const deleteVolunteer = async (req, res) => {
  try {
    const deleted = await Volunteer.findByIdAndDelete(req.params.id);
    res.json({ message: `${deleted.name} has been deleted.` });
  } catch (err) {
    res.status(400).json({ message: err.message });
  }
};

// Filter Volunteers by Name
const filterVolunteersByName = async (req, res) => {
  try {
    const volunteers = await Volunteer.find({ name: { $regex: req.params.name, $options: 'i' } });
    const withImageURL = volunteers.map(v => ({
      ...v._doc,
      image: `${req.protocol}://${req.get('host')}/uploads/${v.image}`,
    }));
    res.json(withImageURL);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

module.exports = {
  registerVolunteer,
  loginVolunteer,
  getAllVolunteers,
  getVolunteerById,
  updateVolunteer,
  deleteVolunteer,
  filterVolunteersByName
};
