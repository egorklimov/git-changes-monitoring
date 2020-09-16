import React, {useEffect, useState} from 'react';
import plotly from 'plotly.js/dist/plotly';
import PlotlyEditor from 'react-chart-editor';
import 'react-chart-editor/lib/react-chart-editor.css';

export default function ChartEditor() {
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

    const dataSourceOptions = Object.keys(dataSources).map(name => ({
        value: name,
        label: name,
    }));

    const [error, setError] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);
    const [state, setState] = useState({data: [], layout: {}, frames: []});

    useEffect(() => {
        const interval = setInterval(() => {
            fetch(
                '/commit/snakecharm/master', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json;charset=utf-8',
                    },
                }).then(response => {
                return response.json();
            }).then((result) => {
                    console.log('RESULT', result);
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
                    result.forEach(record => {
                        console.log(record);
                        data.hash.push(record.hash);
                        data.commitDate.push(Date.parse(record.commitDate, "yyyy-MM-dd HH:mm:ss"));
                        data.authorDate.push(Date.parse(record.authorDate, "yyyy-MM-dd HH:mm:ss"));
                        data.committer.push(record.committer);
                        data.author.push(record.author);
                        data.message.push(record.message);
                        data.commitDateDetails.push(record.commitDateDetails);
                        data.authorDateDetails.push(record.authorDateDetails);
                    })
                    console.log('DATA', data);
                    setIsLoaded(true);
                    setDataSources(data);
                },
                (error) => {
                    setIsLoaded(true);
                    setError(error);
                }
            );
        }, 1000);
        return () => clearInterval(interval);
    }, []);

    if (error) {
        return <div>Ошибка: {error.message}</div>;
    } else if (!isLoaded) {
        return <div>Загрузка...</div>;
    } else {
        return (
            <div className="app">
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
