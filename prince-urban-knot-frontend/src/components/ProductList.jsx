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
            <h2 className="section-title">Products</h2>
            <div className="products-grid">
            {products.map(product => (
                <div className="product-card" key={product.id}>
                    
                    <h3 className="product-name">{product.name}</h3>
                    <img className="product-image"
                    src={product.imageUrl}
                    alt={product.name}/>
                    <p className="product-description">{product.description}</p>
                    <p className="product-price">${product.price}</p>
                </div>
            ))}
             </div>   
        </div>
    );
}

export default ProductList;