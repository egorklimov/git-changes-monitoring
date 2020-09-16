import React from "react";
import {Redirect, Route, Switch} from "react-router";
import ChartEditor from "./ChartEditor";

export default function Root() {
    return (
        <Switch>
            {/*<Route path="/search" component={CheckHistoryPage} />*/}
            <Route
                path="/"
                component={ChartEditor}
            />
            <Redirect to="/"/>
        </Switch>
    );
}