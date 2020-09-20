import React from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import GitHubIcon from '@material-ui/icons/GitHub';
import IconButton from "@material-ui/core/IconButton";
import Fab from "@material-ui/core/Fab";
import CheckIcon from "@material-ui/icons/Check";
import CircularProgress from "@material-ui/core/CircularProgress";
import makeStyles from "@material-ui/core/styles/makeStyles";
import green from "@material-ui/core/colors/green";

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
        alignItems: 'center',
    },
    wrapper: {
        margin: theme.spacing(1),
        position: 'relative',
    },
    fabProgress: {
        color: green[500],
        position: 'absolute',
        top: -2,
        left: -2,
        zIndex: 1,
    },
    buttonProgress: {
        color: green[500],
        position: 'absolute',
        top: '50%',
        left: '50%',
        marginTop: 3,
        marginLeft: 3,
    },
}));

export default function AddOrganizationDialog({handleCloneError}) {
    const classes = useStyles();
    const [open, setOpen] = React.useState(false);

    const [organization, setOrganization] = React.useState();
    const [isCloning, setIsCloning] = React.useState(false);

    const handleClickOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const handleSubmit = () => {
        setIsCloning(true);
        fetch(`https://api.github.com/orgs/${organization}/repos`)
            .then((response) => {
                if (!response.ok) {
                    handleCloneError(response);
                    return [];
                }
                return response.json();
            })
            .then((json) => {
                return json.map((record) => ({url: record.html_url, description: record.description}));
            })
            .then(async (data) => {
                await Promise.all(data.map((repository, idx) => {
                    return fetch('/repository/clone', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json;charset=utf-8',
                        },
                        body: JSON.stringify({
                            url: repository.url + ".git"
                        })
                    }).then((response) => {
                        if (!response.ok) {
                            handleCloneError(response);
                        }
                    })
                }));
                setIsCloning(false);
                setOpen(false);
            });
    };

    const handleChange = (e) => {
        e.preventDefault();
        setOrganization(e.target.value);
    };

    return (
        <div>
            <IconButton onClick={handleClickOpen}>
                <GitHubIcon />
            </IconButton>
            <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Github organization</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        To add all the organization's public repositories from Github, please, enter the organization name.
                        <br/>
                        Name is case sensitive.
                        <br/>
                        It may take a while to get all the repositories, please do not cancel the download.
                    </DialogContentText>
                    <TextField
                        autoComplete="off"
                        autoFocus
                        margin="dense"
                        id="name"
                        placeholder="JetBrains-Research"
                        label="Organization name"
                        fullWidth
                        onChange={handleChange}
                    />
                </DialogContent>
                <DialogActions>
                    {isCloning  ?
                        (
                            <div className={classes.wrapper}>
                                <Fab
                                    aria-label="save"
                                    color="primary"
                                >
                                    <CheckIcon />
                                </Fab>
                                <CircularProgress size={60} className={classes.fabProgress} />
                            </div>
                        ) :
                        (
                            <div>
                                <Button onClick={handleClose} color="primary">
                                    Cancel
                                </Button>
                                <Button onClick={handleSubmit} color="primary">
                                    Add
                                </Button>
                            </div>
                        )
                    }
                </DialogActions>
            </Dialog>
        </div>
    );
}