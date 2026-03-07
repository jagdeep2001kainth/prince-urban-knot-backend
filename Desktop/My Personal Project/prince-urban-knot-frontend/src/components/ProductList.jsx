import { useEffect, useState } from "react";
import { getProducts } from "../services/api";

function ProductList() {

    const [products, setProducts] = useState([]);

    useEffect(() => {

        getProducts().then(data => {
            setProducts(data);
        });

    }, []);

    return (
        <div>
            <h2>Products</h2>

            {products.map(product => (
                <div key={product.id}>
                    <h3>{product.name}</h3>
                    <p>{product.description}</p>
                    <p>${product.price}</p>
                </div>
            ))}

        </div>
    );
}

export default ProductList;