package com.example.algoritmia.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.algoritmia.Managers.GraphManager;
import com.example.algoritmia.Objects.Link;
import com.example.algoritmia.Objects.Vertice;
import com.example.algoritmia.R;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphListFragment extends Fragment {

    // TreeNodeWrapperView treeView;
    // RecyclerView recyclerView;
    View view;
    TextView txtNombreCirculo1;
    TextView txtNombreCirculo2;
    TextView txtPosXCirculo1;
    TextView txtPosXCirculo2;
    TextView txtPosYCirculo1;
    TextView txtPosYCirculo2;
    LinearLayout treeView;
    boolean isThisObjectLoaded = false;
    public GraphListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_graph_list, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GraphManager.getVertices().size() > 0) {
            isThisObjectLoaded = true;
            txtNombreCirculo1 = (TextView) view.findViewById(R.id.circle1_name);
            txtNombreCirculo2 = (TextView) view.findViewById(R.id.circle2_name);
            txtPosXCirculo1   = (TextView) view.findViewById(R.id.circle1_xpos);
            txtPosXCirculo2   = (TextView) view.findViewById(R.id.circle2_xpos);
            txtPosYCirculo1   = (TextView) view.findViewById(R.id.circle1_ypos);
            txtPosYCirculo2   = (TextView) view.findViewById(R.id.circle2_ypos);

            Vertice circulo1;
            Vertice circulo2;
            Link conexion = null;

            for (Vertice vertice : GraphManager.getVertices()){
                circulo1 = vertice;
                for(Link arista: vertice.getLinks()){
                    circulo2 = arista.getDestination();
                    if(conexion == null)
                        conexion = arista;
                    if(circulo1 != circulo2){
                        if(arista.getContent().getDistance() < conexion.getContent().getDistance()){
                            conexion = arista;
                        }
                    }
                }
            }
            GraphManager.setSmallestLink(conexion);
            if(conexion != null) {
                txtNombreCirculo1.setText("Circulo " + conexion.getOrigin().getName());
                txtNombreCirculo2.setText("Circulo " + conexion.getDestination().getName());
                txtPosXCirculo1.setText("Pos X: " + conexion.getOrigin().getCircle().getCenterPoint().x);
                txtPosYCirculo1.setText("Pos Y: " + conexion.getOrigin().getCircle().getCenterPoint().y);
                txtPosXCirculo2.setText("Pos X: " + conexion.getDestination().getCircle().getCenterPoint().x);
                txtPosYCirculo2.setText("Pos Y: " + conexion.getDestination().getCircle().getCenterPoint().y);
                GraphManager.setLoadedGraphData(true);
            }
            else{
                GraphManager.setLoadedGraphData(false);
            }
            TreeNode root = TreeNode.root();
            treeView = (LinearLayout) view.findViewById(R.id.tree_view);
            ArrayList<TreeNode> vertexNodes = new ArrayList<>();
            ArrayList<TreeNode> edgeNodes;
            int i = 0;
            TreeNode parent = new TreeNode("").setExpanded(true);

            for (Vertice vertice : GraphManager.getVertices()) {

                vertexNodes.add( new TreeNode(new Content("" + vertice.getCircle().getCenterPoint().x,
                        "" + vertice.getCircle().getCenterPoint().y, vertice.getName(), 25))
                        .setViewHolder(new Item(getContext())).setExpanded(false));

                edgeNodes = new ArrayList<>();
                for(Link link : vertice.getLinks()){
                    edgeNodes.add( new TreeNode(new Content("" + link.getDestination().getCircle().getCenterPoint().x,
                            "" + link.getDestination().getCircle().getCenterPoint().y, link.getDestination().getName(), 15))
                            .setViewHolder(new Item(getContext())).setExpanded(false));
                }
                vertexNodes.get(i).addChildren(edgeNodes);
                i++;
            }
            parent.addChildren(vertexNodes);
            root.addChild(parent);
            AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
            treeView.addView(tView.getView());
        }
    }

    private void restartViews() {
        if(isThisObjectLoaded) {
            treeView.removeAllViews();
            treeView.removeAllViewsInLayout();
            txtNombreCirculo1.setText("Circulo ");
            txtNombreCirculo2.setText("Circulo ");
            txtPosXCirculo1.setText("Pos X: ");
            txtPosYCirculo1.setText("Pos Y: ");
            txtPosXCirculo2.setText("Pos X: ");
            txtPosYCirculo2.setText("Pos Y: ");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        restartViews();
    }

    public class Item extends TreeNode.BaseNodeViewHolder<Content> {

        public Item(Context context) {
            super(context);
        }

        @Override
        public View createNodeView(TreeNode node, Content value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.graph_item, null, false);

            TextView nombre = (TextView) view.findViewById(R.id.circle_name);
            TextView posX = (TextView) view.findViewById(R.id.circle_xpos);
            TextView posY = (TextView) view.findViewById(R.id.circle_ypos);
            nombre.setTextColor(Color.BLACK);
            posY.setTextColor(Color.BLACK);
            posX.setTextColor(Color.BLACK);
            nombre.setTextSize(value.textSize);
            posY.setTextSize(value.textSize);
            posX.setTextSize(value.textSize);
            nombre.setVisibility(View.VISIBLE);
            posX.setVisibility(View.VISIBLE);
            posY.setVisibility(View.VISIBLE);
            nombre.setText(nombre.getText() + value.name);
            posX.setText(posX.getText() + value.posX);
            posY.setText(posY.getText() + value.posY);

            return view;
        }
    }

    public static class Content {
        String posX;
        String posY;
        String name;
        float textSize;

        public Content(String posX, String posY, String name, float textSize) {
            this.posX = posX;
            this.posY = posY;
            this.name = name;
            this.textSize = textSize;
        }
    }
}