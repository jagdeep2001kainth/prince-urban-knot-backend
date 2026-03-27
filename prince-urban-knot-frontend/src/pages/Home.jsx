// src/pages/Home.jsx
import React, { useEffect, useState } from "react";
import { getProducts } from "../services/api"; // ✅ Use the named fetch function
import ProductCard from "../components/ProductList";

const Home = () => {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    getProducts()
      .then(data => setProducts(data)) // ✅ No need for .data with standard fetch
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