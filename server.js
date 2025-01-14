const express = require('express'); // Import Express
const cors = require('cors'); // Import CORS middleware
const Stripe = require('stripe'); // Import Stripe SDK

const stripe = Stripe('sk_test_51QfwnlFTdqp1BVfOCnwqUZnmOr1YBOTR6Huz4x2g6UUE80LP3NBBDCI6HtydFQV5iadBWKJvFoVmgCqE5dx1jd4s00a7bnJiKA'); // Stripe Secret Key
const app = express();
const port = 3000;

// Middleware
app.use(cors()); // Enable CORS
app.use(express.json()); // Parse JSON request bodies

// Root route
app.get('/', (req, res) => {
    res.send('Server is running. Use /create-payment-intent for payment intent creation.');
});

// Create Payment Intent endpoint
app.post('/create-payment-intent', async (req, res) => {
    try {
        const { amount } = req.body; // Extract amount from request body
        if (!amount) {
            return res.status(400).send({ error: 'Amount is required' });
        }

        // Create a PaymentIntent with the specified amount
        const paymentIntent = await stripe.paymentIntents.create({
            amount, // Amount in cents
            currency: 'cad', // Currency
        });

        // Send the client secret to the client
        res.json({ clientSecret: paymentIntent.client_secret });
    } catch (error) {
        console.error('Error creating payment intent:', error.message);
        res.status(500).send({ error: error.message });
    }
});

// Start the server
app.listen(port, '0.0.0.0', () => {
    console.log(`Server running on http://0.0.0.0:${port}`);
});
