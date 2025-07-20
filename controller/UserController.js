const express = require('express');
require('dotenv').config();
const User= require('../model/User') 
const multer = require('../middleware/multer')

const router = express.Router();
const bcrypt = require("bcrypt");
const jwt = require('jsonwebtoken');

router.get('/', function(req, res) {
    res.send("This is User Home Page....!!!")
});

router.post('/foodRegisteruser', multer.single('image'), async function(req, res) {
  
    try {
      const { userid, name, password, contact } = req.body;
      if (!req.file) return res.status(400).json({ error: 'Image is required' });

      const hash= await bcrypt.hash(password, 10);
      
  
      const newuser = new User({
        userid,
        name,
        password:hash,
        contact,
        image: req.file.filename, // store filename or full path if needed
      });
  
      const saved = await newuser.save();
      res.status(201).json(saved);
    } catch (err) {
      res.status(500).json({ error: err.message });
    }
});

router.post('/login', async function (req, res) {
     try {
        const { userid, password} = req.body;
        
        if (!userid|| !password ) {
            return res.status(400).json({
                message: "Something is missing",
                success: false
            });
        };
        let user = await User.findOne({ userid });
        console.log(user)
        if (!user) {
            return res.status(400).json({
                message: "Incorrect email or password.",
                success: false,
            })
        }
        const isPasswordMatch = await bcrypt.compare(password, user.password);
        if (!isPasswordMatch) {
            return res.status(400).json({
                message: "Incorrect email or password.",
                success: false,
            })
        };
       
        const tokenData = {
            userId: user._id
        }
        const token = await jwt.sign(tokenData, process.env.SECRET_KEY, { expiresIn: '1d' });

        user = {
            _id: user._id,
            name:user.name,
            userid: user.userid,
            contact: user.contact,
        }

        return res.status(200).cookie("token", token, { maxAge: 1 * 24 * 60 * 60 * 1000, httpsOnly: true, sameSite: 'strict' }).json({
            message: `Welcome back ${user.name}`,
            user,
            success: true
        })
    } catch (error) {
        console.log(error);
    }
});

router.get('/profile/:id', async function (req, res) {
    try{
        const data = await User.findById(req.params.id);
        res.json(data)
    }
    catch(error){
        res.status(500).json({message: error.message})
    }
});

router.get('/getAllFoods',async function (req, res) {
    try {
      const users = await User.find();
  
      // Add full image URL to each product
      const usersWithImageUrl = users.map(p => ({
        ...p._doc,
        image: `${req.protocol}://${req.get('host')}/uploads/${p.image}`,
      }));
  
      res.json(usersWithImageUrl);
    } catch (err) {
      res.status(500).json({ error: err.message });
    }
});

router.patch('/update/:id',async function (req, res) {
   try {
        const id = req.params.id;
        const updatedData = req.body;
        const options = { new: true };

        const result = await User.findByIdAndUpdate(
            id, updatedData, options
        )

        res.send(result)
    }
    catch (error) {
        res.status(400).json({ message: error.message })
    }
});

router.delete('/delete/:id',async function (req, res) {
    try {
        const id = req.params.id;
        const data = await User.findByIdAndDelete(id)
        res.send(`Document with ${data.name} has been deleted..`)
    }
    catch (error) {
        res.status(400).json({ message: error.message })
    }
});

router.get('/filterByName/:name',async function (req, res) {
    try {
      const users = await User.find({name:{$regex:req.params.name,$options:'i'}});
  
      // Add full image URL to each product
      const usersWithImageUrl = users.map(p => ({
        ...p._doc,
        image: `${req.protocol}://${req.get('host')}/uploads/${p.image}`,
      }));
  
      res.json(usersWithImageUrl);
    } catch (err) {
      res.status(500).json({ error: err.message });
    }
});

module.exports = router