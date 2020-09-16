import React from 'react';
import {BrowserRouter} from "react-router-dom";
import Root from "./Root";


export default function Route() {
    return (
        <BrowserRouter>
            <Root />
        </BrowserRouter>
    );
}