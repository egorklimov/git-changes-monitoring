import React from "react";
import PropTypes from "prop-types";
import {useHistory} from "react-router-dom";
import {makeStyles} from "@material-ui/core/styles";
import TreeView from "@material-ui/lab/TreeView";
import TreeItem from "@material-ui/lab/TreeItem";
import Typography from "@material-ui/core/Typography";
import ArrowDropDownIcon from "@material-ui/icons/ArrowDropDown";
import ArrowRightIcon from "@material-ui/icons/ArrowRight";
import GitHubIcon from "@material-ui/icons/GitHub";
import AccountTreeIcon from "@material-ui/icons/AccountTree";
import AppsIcon from "@material-ui/icons/Apps";
import GroupIcon from "@material-ui/icons/Group";

const useTreeItemStyles = makeStyles((theme) => ({
  root: {
    color: theme.palette.text.secondary,
  },
  content: {
    color: theme.palette.text.secondary,
    borderTopRightRadius: theme.spacing(2),
    borderBottomRightRadius: theme.spacing(2),
    paddingRight: theme.spacing(1),
    fontWeight: theme.typography.fontWeightMedium,
    fontSize: "1rem !important",
    "$expanded > &": {
      fontWeight: theme.typography.fontWeightRegular,
    },
  },
  group: {
    "& $content": {
      paddingLeft: theme.spacing(2),
    },
  },
  expanded: {},
  selected: {},
  label: {
    fontSize: "inherit",
    fontWeight: "inherit",
    color: "inherit",
  },
  labelRoot: {
    display: "flex",
    alignItems: "center",
    padding: theme.spacing(0.5, 0),
  },
  labelIcon: {
    marginRight: theme.spacing(1),
  },
  labelText: {
    fontSize: "inherit",
    fontWeight: "inherit",
    flexGrow: 1,
  },
}));

function StyledTreeItem(props) {
  const classes = useTreeItemStyles();
  const { labelText, labelIcon: LabelIcon, labelInfo, color, bgColor, ...other } = props;

  return (
      <TreeItem
          label={
            <div className={classes.labelRoot}>
              <LabelIcon color="inherit" className={classes.labelIcon} />
              <Typography variant="body2" className={classes.labelText}>
                {labelText}
              </Typography>
              <Typography variant="caption" color="inherit">
                {labelInfo}
              </Typography>
            </div>
          }
          classes={{
            root: classes.root,
            content: classes.content,
            expanded: classes.expanded,
            selected: classes.selected,
            group: classes.group,
            label: classes.label,
          }}
          {...other}
      />);
}

StyledTreeItem.propTypes = {
  labelIcon: PropTypes.elementType.isRequired,
  labelInfo: PropTypes.string,
  labelText: PropTypes.string.isRequired,
};

const useStyles = makeStyles({
  root: {
    height: 264,
    flexGrow: 1,
    maxWidth: 400,
  },
});

export default function Tree(props) {
  const history = useHistory();

  const {organizations} = props;

  const onBranchLabelClick = (repositoryName, branchName) => (clickEvent) => {
    clickEvent.preventDefault();
    history.push(`/${repositoryName}/${branchName}`);
  };

  const classes = useStyles();
  return (
      <TreeView
          className={classes.root}
          defaultExpanded={["3"]}
          defaultCollapseIcon={<ArrowDropDownIcon />}
          defaultExpandIcon={<ArrowRightIcon />}
          defaultEndIcon={<div style={{ width: 24 }} />}
      >
        <StyledTreeItem
            nodeId="0"
            labelText="Repositories"
            labelIcon={AppsIcon}
            labelInfo={String(organizations.length)}
        >
          {organizations.map((organization, idx) =>
              <StyledTreeItem
                  nodeId={idx + organization.name}
                  labelText={organization.name}
                  labelIcon={GroupIcon}
                  labelInfo={String(organization.repositories.length)}
                  color="#1a73e8"
                  bgColor="#e8f0fe"
              >
                {organization.repositories.map((repository, idx) =>
                    <StyledTreeItem
                        nodeId={idx + organization.name + repository.name}
                        labelText={repository.name}
                        labelIcon={GitHubIcon}
                        labelInfo={String(repository.branches.length)}
                        color="#1a73e8"
                        bgColor="#e8f0fe"
                    >
                      {repository.branches.map((branch, idx) =>
                          <StyledTreeItem
                              nodeId={idx + organization.name + repository.name + branch.shortName}
                              labelIcon={AccountTreeIcon}
                              labelText={branch.shortName}
                              color="#3c8039"
                              onLabelClick={onBranchLabelClick(repository.name, branch.shortName)}
                          />
                          )}
                    </StyledTreeItem>
                )}
              </StyledTreeItem>
          )}
        </StyledTreeItem>
      </TreeView>
  );
}