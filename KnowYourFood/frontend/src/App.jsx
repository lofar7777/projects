import { BrowserRouter, Routes, Route } from "react-router-dom";

import Home from "./pages/Home";
import About from "./pages/About";
import Product from "./pages/Product";
import NotFound from "./pages/NotFound";

// function Home(){
//   return(
//     <h1>Home is working!</h1>
//   )
// }

function App(){
  return(
    <BrowserRouter>
      <Routes>
        <Route path="/" element = {<Home />} />
        <Route path="/about" element = {<About />} />
        <Route path="/product" element = {<Product />} />
        <Route path="*" element = {<NotFound />} />
      </Routes>
    </BrowserRouter>
  );

}

// function App(){
//   return (
//     <BrowserRouter>
//     <h1>Browser Router works!
//     </h1>
//     {/* <Routes>
//       <Route path="/" element = {<Home />} />
//     </Routes> */}
//     </BrowserRouter>
//   );
// }

export default App;