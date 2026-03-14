// src/pages/Home.jsx
import React, { useEffect, useState } from "react";
import api from "../services/api";
import ProductCard from "../components/ProductList";

const Home = () => {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    api.get("/products")
       .then(res => setProducts(res.data))
       .catch(err => console.error(err));
  }, []);

  return (
    <div className="product-list">
      {products.map(product => (
        <ProductCard key={product.id} product={product} />
      ))}
    </div>
  );
};

export default Home;