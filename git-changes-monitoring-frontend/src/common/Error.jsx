import React from "react";
import Container from "@material-ui/core/Container";
import ErrorOutlineIcon from "@material-ui/icons/ErrorOutline";
import Typography from "@material-ui/core/Typography";
import {createStyles, makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles((theme) =>
    createStyles({
        container: {
            marginTop: "25vh",
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
        },
        svg: {
            transform: 'scale(2.0)',
            width: '100%',
            height: '100%',
        },
        errorText: {
            textAlign: 'center',
            marginTop: '70px',
        },
    }),
);

export default function Error(props) {
    const classes = useStyles();
    const { error } = props;
    return (
        <div className="app">
            <Container
                className={classes.container}
                maxWidth="sm"
            >
                <ErrorOutlineIcon
                    className={classes.svg}
                    color='primary'
                />
                <Typography variant="h6" className={classes.errorText}>
                    {error}
                </Typography>
            </Container>
        </div>
    );
}