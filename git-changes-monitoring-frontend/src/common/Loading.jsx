import React from "react";
import AppBar from '@material-ui/core/AppBar';
import {createStyles, makeStyles} from "@material-ui/core/styles";
import {LinearProgress, Toolbar} from "@material-ui/core";
import Typography from "@material-ui/core/Typography";
import Breadcrumbs from "@material-ui/core/Breadcrumbs";
import Link from "@material-ui/core/Link";
import HomeIcon from "@material-ui/icons/Home";

const useStyles = makeStyles((theme) =>
    createStyles({
        appBar: {
            alignItems: 'center',
            backgroundColor: '#172b4d',
        },
        typography: {
            color: "#e5ecf5",
        },
        link: {
            display: 'flex',
        },
        flex: {
            display: 'flex',
        },
        icon: {
            marginRight: theme.spacing(0.5),
            width: 20,
            height: 20,
        },
        progress: {
            bar: '#03b6fc',
        },
    }),
);

export default function Loading(props) {
    const classes = useStyles();
    const { repository, branch } = props;
    return (
        <div>
            <AppBar className={classes.appBar} position="static">
                <Toolbar>
                        {repository && branch ?
                            (
                                <Breadcrumbs className={classes.typography} aria-label="breadcrumb">
                                    <Link color="inherit" href="/" className={classes.link}>
                                        <HomeIcon className={classes.icon} />
                                        <Typography>
                                            home
                                        </Typography>
                                    </Link>
                                    <Typography>
                                        {repository}
                                    </Typography>
                                    <Typography>
                                        {branch}
                                    </Typography>
                                </Breadcrumbs>
                            ) :
                            (
                                <Breadcrumbs className={classes.typography} aria-label="breadcrumb">
                                    <div className={classes.flex}>
                                        <HomeIcon className={classes.icon} />
                                        <Typography>
                                            git-changes-monitoring
                                        </Typography>
                                    </div>
                                </Breadcrumbs>
                            )
                        }
                </Toolbar>
            </AppBar>
            <LinearProgress className={classes.progress} />
        </div>
    );
}