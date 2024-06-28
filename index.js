const express = require('express');
const mysql = require('mysql');
const QRCode = require('qrcode');
const path = require('path');

const app = express();
const port = 3000;

const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'x'
});

db.connect(err => {
    if (err) {
        console.error('Error connecting to the database:', err);
        return;
    }
    console.log('Connected to the database.');
    generateQrCodesForAllProducts();
});

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

app.get('/product/:id', (req, res) => {
    const productId = req.params.id;

    db.query('SELECT * FROM product WHERE id = ?', [productId], (err, result) => {
        if (err) {
            console.error('Error fetching product:', err);
            res.status(500).send('Internal Server Error');
            return;
        }

        if (result.length === 0) {
            res.status(404).send('Product not found');
            return;
        }

        const product = result[0];
        res.render('product', { product });
    });
});

app.listen(port, () => {
    console.log(`Server is running at http://localhost:${port}`);
});

function generateQrCodesForAllProducts() {
    db.query('SELECT id FROM product', (err, results) => {
        if (err) {
            console.error('Error fetching product IDs:', err);
            return;
        }

        results.forEach(row => {
            const productId = row.id;
            const productUrl = `http://localhost:${port}/product/${productId}`;

            QRCode.toDataURL(productUrl, (err, url) => {
                if (err) {
                    console.error('Error generating QR code:', err);
                    return;
                }

                db.query('UPDATE product SET qr_kod = ? WHERE id = ?', [url, productId], (err) => {
                    if (err) {
                        console.error('Error updating product with QR code:', err);
                    } else {
                        console.log(`QR code generated and saved for product ID ${productId}.`);
                    }
                });
            });
        });
    });
}
