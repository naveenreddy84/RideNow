const express = require('express');  // Importing Express to create the server
const Stripe = require('stripe');   // Importing Stripe SDK to interact with Stripe API
const stripe = Stripe('sk_test_51QfwnlFTdqp1BVfOCnwqUZnmOr1YBOTR6Huz4x2g6UUE80LP3NBBDCI6HtydFQV5iadBWKJvFoVmgCqE5dx1jd4s00a7bnJiKA');  // Replace with Stripe Secret Key

const app = express();
const port = 3000;

app.use(express.json());

app.get('/create-payment-intent', async (req, res) => {
    try {
        const paymentIntent = await stripe.paymentIntents.create({
            amount: 1000,  // The amount in cents (e.g., 10 USD)
            currency: 'cad',  // Your currency
        });

        res.json({ clientSecret: paymentIntent.client_secret });
    } catch (error) {
        res.status(500).send({ error: error.message });
    }
});

app.listen(3000, '0.0.0.0', () => {
    console.log('Server running on http://0.0.0.0:3000');
});