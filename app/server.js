const express = require("express");
const stripe = require("stripe")("sk_test_51QfwnlFTdqp1BVfOCnwqUZnmOr1YBOTR6Huz4x2g6UUE80LP3NBBDCI6HtydFQV5iadBWKJvFoVmgCqE5dx1jd4s00a7bnJiKA");
const cors = require("cors");

const app = express();
app.use(cors());
app.use(express.json());

app.post("/create-payment-intent", async (req, res) => {
    const { amount } = req.body;

    try {
        const paymentIntent = await stripe.paymentIntents.create({
            amount,
            currency: "usd",
            payment_method_types: ["card"],
        });

        res.send({
            clientSecret: paymentIntent.client_secret,
        });
    } catch (error) {
        console.error(error);
        res.status(500).send({ error: error.message });
    }
});

const PORT = 4242;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
