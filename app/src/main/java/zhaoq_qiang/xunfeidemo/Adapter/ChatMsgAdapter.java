package zhaoq_qiang.xunfeidemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import zhaoq_qiang.xunfeidemo.Bean.PlayerInfo;
import zhaoq_qiang.xunfeidemo.R;
import zhaoq_qiang.xunfeidemo.View.CircleImageView;

public class ChatMsgAdapter extends BaseAdapter {
    private List<PlayerInfo> playerInfoList;
    private LayoutInflater layoutInflater;

    public ChatMsgAdapter(Context context, List<PlayerInfo> infoList) {
        this.playerInfoList = infoList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return playerInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return playerInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.send_message_item, null);
            viewHolder.otherLayout = (LinearLayout) view.findViewById(R.id.other_layout);
            viewHolder.otherText = (TextView) view.findViewById(R.id.other_content);
            viewHolder.otherIcon = (CircleImageView) view.findViewById(R.id.other_headview);
            viewHolder.mineLayout = (LinearLayout) view.findViewById(R.id.mine_layout);
            viewHolder.mineText = (TextView) view.findViewById(R.id.mine_content);
            viewHolder.mineIcon = (CircleImageView) view.findViewById(R.id.mine_headview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        PlayerInfo playerInfo = playerInfoList.get(i);
        if (playerInfo.isMine()) {
            viewHolder.otherLayout.setVisibility(View.GONE);
            viewHolder.mineLayout.setVisibility(View.VISIBLE);
            viewHolder.mineText.setText(playerInfo.getTextContent());
        } else {
            viewHolder.otherLayout.setVisibility(View.VISIBLE);
            viewHolder.mineLayout.setVisibility(View.GONE);
            viewHolder.otherText.setText(playerInfo.getTextContent());
        }

        return view;
    }

    private class ViewHolder {
        private LinearLayout otherLayout;
        private CircleImageView otherIcon;
        private TextView otherText;
        private LinearLayout mineLayout;
        private CircleImageView mineIcon;
        private TextView mineText;
    }
}
