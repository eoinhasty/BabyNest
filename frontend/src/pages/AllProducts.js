import {useEffect, useState} from "react";
import DataTable from "datatables.net-react";
import DT from 'datatables.net-dt';
import '../css/AllProducts.css';

DataTable.use(DT);

function AllProducts() {
    const [products, setProducts] = useState([]);
    const [minPrice, setMinPrice] = useState(0);
    const [maxPrice, setMaxPrice] = useState(Infinity);
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState("All");
    const [filteredProducts, setFilteredProducts] = useState([]);

    //Fetch data from the Backend
    const fetchApi = async () => {
        let productsUrl = 'http://localhost:8888/api/products/';
        const productResponse = await fetch(productsUrl);
        let categoriesUrl = 'http://localhost:8888/api/categories/';
        const categoriesResponse = await fetch(categoriesUrl);

        const productData = await productResponse.json();
        const categoriesData = await categoriesResponse.json();

        setProducts(productData);
        setCategories(categoriesData);
    }

    //Call fetchApi when the component is mounted
    useEffect(() => {
        fetchApi();
    }, []);

    //Filter products based on the selected category, minPrice and maxPrice
    useEffect(() => {
        if (products.length > 0) {
            const filtered = products.filter((product) => {
                const price = parseFloat(product.price) || 0;

                switch (selectedCategory) {
                    case "All":
                        return price >= minPrice && price <= maxPrice;
                    default:
                        return product.category &&
                            product.category.name === selectedCategory &&
                            price >= minPrice &&
                            price <= maxPrice;
                }
            });
            setFilteredProducts(filtered);
        } else {
            setFilteredProducts([]);
        }
    }, [products, minPrice, maxPrice, selectedCategory]);

    // //Initialize DataTable
    // useEffect(() => {
    //     if (filteredProducts.length > 0 && !$.fn.DataTable.isDataTable("#products")) {
    //         $("#products").DataTable();
    //     }
    //
    //     return () => {
    //         if ($.fn.dataTable.isDataTable("#products")) {
    //             $("#products").DataTable().destroy();
    //         }
    //     };
    // }, []);

    //Format price to EUR
    const formatPrice = (price) => {
        return new Intl.NumberFormat('en-IE', {style: 'currency', currency: 'EUR'}).format(price);
    }

    const columns = [
        { data: "name", title: "Name" },
        { data: "description", title: "Description" },
        {
            data: null,
            title: "Price",
            render: data => formatPrice(data.price),
        },
        { data: "category.name", title: "Category" },
        {
            data: null,
            title: "Image",
            render: (data, type, row) =>
                `<a href="/products/${row.productId}">
                    <img
                        src="http://localhost:8888/assets/images/thumbs/${row.productId}/${row.feature_image}"
                        alt="${row.name}"
                        style="width: 100px;"
                    />
                </a>`
        },
    ];

    //Render the component
    return (
        <div className={"App"}>
            <form>
                <div className={"input-wrapper"}>
                    <label htmlFor={"minPrice"}>Min Price:</label>
                    <input
                        type={"number"}
                        id={"minPrice"}
                        name={"minPrice"}
                        value={minPrice === 0 ? "" : minPrice}
                        step={0.5}
                        onChange={(minPriceInput) => setMinPrice(parseFloat(minPriceInput.target.value) || 0)}
                    />
                </div>

                <div className={"input-wrapper"}>
                    <label htmlFor={"maxPrice"}>Max Price:</label>
                    <input
                        type={"number"}
                        id={"maxPrice"}
                        name={"maxPrice"}
                        value={maxPrice === Infinity ? "" : maxPrice}
                        onChange={(maxPriceInput) => setMaxPrice(parseFloat(maxPriceInput.target.value) || Infinity)}
                    />
                </div>

                <div className={"input-wrapper"}>
                    <label htmlFor={"category"}>Category:</label>
                    <select
                        id={"category"}
                        name={"category"}
                        value={selectedCategory}
                        onChange={(categoryInput) => setSelectedCategory(categoryInput.target.value)}
                    >
                        <option value={"All"}>All</option>
                        {categories.map((category) => (
                            <option key={category.categoryId} value={category.name}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </div>
            </form>

            <DataTable
                data={filteredProducts}
                className={"display"}
                columns={columns}
                options={{
                    paging: true,
                    searching: true,
                    ordering: false,
            }}>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Category</th>
                        <th>Image</th>
                    </tr>
                </thead>
            </DataTable>
        </div>
    )
}

// <table id={"products"} className={"display"}>
//     <thead>
//     <tr>
//         <th>Name</th>
//         <th>Description</th>
//         <th>Price</th>
//         <th>Category</th>
//         <th>Image</th>
//     </tr>
//     </thead>
//     <tbody>
//     {filteredProducts.length > 0 ? (
//         filteredProducts.map((product) => (
//             <tr key={product.productId}>
//                 <td>{product.name}</td>
//                 <td>{product.description}</td>
//                 <td>{formatPrice(product.price)}</td>
//                 <td>{product.category?.name || "Uncategorized"}</td>
//                 <td>
//                     <Link to={`/products/${product.productId}`}>
//                         <img
//                             src={`http://localhost:8888/assets/images/thumbs/${product.productId}/${product.feature_image}`}
//                             alt={product.name}
//                             style={{width: "100px"}}
//                         />
//                     </Link>
//                 </td>
//             </tr>
//         ))
//     ) : (
//         <tr>
//             <td colSpan={5}>No products found</td>
//         </tr>
//     )}
//     </tbody>
// </table>

export default AllProducts;