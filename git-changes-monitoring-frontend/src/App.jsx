import React from "react";
import {BrowserRouter} from "react-router-dom";
import {Redirect, Route, Switch} from "react-router";
import ChartEditor from "./editor/ChartEditor";
import Home from "./home/Home";

export default function App() {
    return (
        <BrowserRouter>
            <Switch>
                <Route
                    path="/:repository/:branch/"
                    render={
                        ({match}) =>
                            <ChartEditor
                                repository={match.params.repository}
                                branch={match.params.branch}
                            />
                    }
                />
                <Route
                    path="/"
                    component={Home}
                />
                <Redirect
                    to="/"
                />
            </Switch>
        </BrowserRouter>
    );
}