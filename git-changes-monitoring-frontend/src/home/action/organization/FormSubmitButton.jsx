import React from 'react';
import clsx from 'clsx';
import {createStyles, makeStyles} from '@material-ui/core/styles';
import {green} from '@material-ui/core/colors';
import Button from '@material-ui/core/Button';
import Progress from "../../../common/Progress";

const useStyles = makeStyles((theme) =>
    createStyles({
        root: {
            display: 'flex',
            alignItems: 'center',
        },
        wrapper: {
            margin: theme.spacing(1),
            position: 'relative',
        },
        buttonSuccess: {
            backgroundColor: green[500],
            '&:hover': {
                backgroundColor: green[700],
            },
        },
        fabProgress: {
            color: green[500],
            position: 'absolute',
            top:-2,
            left: -2,
            zIndex: 1,
        },
    }),
);

export default function FormSubmitButton(props) {
    const { org, onLoad } = props;
    const classes = useStyles();
    const [loading, setLoading] = React.useState(false);
    const [success, setSuccess] = React.useState(false);
    const [progress, setProgress] = React.useState({current: 0, size: 100});

    const buttonClassname = clsx({
        [classes.buttonSuccess]: success,
    });

    const handleButtonClick = async () => {

        if (!loading) {
            setSuccess(false);
            setLoading(true);

            const response = await fetch(`https://api.github.com/orgs/${org}/repos`)
                .then((response) => {
                    console.log('RESP', response);
                    return response.json();
                }).then((json) => {
                    setProgress({current: 0, size: json.length});
                    json.forEach((record) => {
                        setProgress({current: progress.current + 1, size: json.length});
                        onLoad(record.html_url, record.description);
                    })
                    return json;
                });

            await Promise.all([
                response,
                () => setSuccess(true),
                () => setLoading(false),
                () => props.onClick(),
            ]);
        }
    };


    return (
        <div className={classes.root}>
            {loading ?
                (
                    <div className={classes.wrapper}>
                        <Progress value={progress && progress.current ? (progress.current / progress.size) * 100 : 0} />
                    </div>
                ) :
                (
                    <div className={classes.wrapper}>
                        <Button
                            variant="contained"
                            color="primary"
                            className={buttonClassname}
                            disabled={loading}
                            onClick={handleButtonClick}
                        >
                            Add
                        </Button>
                    </div>
                )
            }
        </div>
    );
}