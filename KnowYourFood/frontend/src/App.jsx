import { BrowserRouter, Routes, Route } from "react-router-dom";

import MainLayout from "./layouts/MainLayout";

import Home from "./pages/Home";
import About from "./pages/About";
import Product from "./pages/Product";
import NotFound from "./pages/NotFound";

function App(){
  return(
    <BrowserRouter>
      <Routes>
        <Route path="/" element = {<MainLayout> <Home /> </MainLayout>} />
        <Route path="/about" element = {<MainLayout> <About /> </MainLayout>} />
        <Route path="/product" element = {<MainLayout> <Product /> </MainLayout>} />
        <Route path="*" element = {<MainLayout> <NotFound /> </MainLayout>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;