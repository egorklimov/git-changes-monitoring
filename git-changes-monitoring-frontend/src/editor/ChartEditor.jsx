import React, {useEffect, useState} from "react";
import plotly from "plotly.js/dist/plotly";
import PlotlyEditor from "react-chart-editor";
import "react-chart-editor/lib/react-chart-editor.css";
import Loading from "../common/Loading";
import ApplicationBar from "../common/ApplicationBar";
import Error from "../common/Error";

export default function ChartEditor(props) {
    const {repository, branch} = props;
    const config = {editable: true};

    const [dataSources, setDataSources] = useState({
        hash: [],
        commitDate: [],
        authorDate: [],
        committer: [],
        author: [],
        message: [],
        commitDateDetails: [],
        authorDateDetails: [],
    });

    const dataSourceOptions = Object.keys(dataSources).map((name) => ({
        value: name,
        label: name,
    }));

    const [error, setError] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);
    const [state, setState] = useState({data: [], layout: {}, frames: []});

    const loadCommits = (repository, branch) => {
        fetch(
            `/commit/${repository}/${branch}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json;charset=utf-8",
                },
            }).then((response) => {
                if (!response.ok) {
                    throw {message: response.status};
                }
                return response.json();
            }).then((result) => {
                const data = {
                    hash: [],
                    commitDate: [],
                    authorDate: [],
                    committer: [],
                    author: [],
                    message: [],
                    commitDateDetails: [],
                    authorDateDetails: [],
                }
                result.forEach((record) => {
                    data.hash.push(record.hash);
                    data.commitDate.push(Date.parse(record.commitDate, "yyyy-MM-dd HH:mm:ss"));
                    data.authorDate.push(Date.parse(record.authorDate, "yyyy-MM-dd HH:mm:ss"));
                    data.committer.push(record.committer);
                    data.author.push(record.author);
                    data.message.push(record.message);
                    data.commitDateDetails.push(record.commitDateDetails);
                    data.authorDateDetails.push(record.authorDateDetails);
                })
                setIsLoaded(true);
                setDataSources(data);
            },
            (error) => {
                setIsLoaded(true);
                if (error.message === 404) {
                    setError(`Repository ${repository} or branch ${branch} not found.`);
                } else {
                    setError(`Something went wrong. Received ${error.message} from backend on loading commit history.`);
                }
            }
        );
    };

    useEffect(() => {
        loadCommits(repository, branch);
        const interval = setInterval(() => {
            loadCommits(repository, branch);
        }, 120000);
        return () => clearInterval(interval);
    }, []);

    if (error) {
        return (
            <div>
                <ApplicationBar
                    repository={repository}
                    branch={branch}
                />
                <Error
                    error={error}
                />
            </div>
        );
    } else if (!isLoaded) {
        return <Loading repository={repository} branch={branch}/>;
    } else {
        return (
            <div className="app">
                <ApplicationBar
                    repository={repository}
                    branch={branch}
                />
                <PlotlyEditor
                    data={state.data}
                    layout={state.layout}
                    config={config}
                    frames={state.frames}
                    dataSources={dataSources}
                    dataSourceOptions={dataSourceOptions}
                    plotly={plotly}
                    onUpdate={(data, layout, frames) =>
                        setState({data, layout, frames})
                    }
                    useResizeHandler
                    debug
                    advancedTraceTypeSelector
                />
            </div>
        );
    }
}
