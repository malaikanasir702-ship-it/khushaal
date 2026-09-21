import React from 'react';
import { LucideIcon } from 'lucide-react';

interface StatCardProps {
  title: string;
  urduTitle?: string;
  value: string | number;
  subtitle?: string;
  icon: LucideIcon;
  trend?: {
    value: string;
    isPositive?: boolean;
  };
  onClick?: () => void;
}

export const StatCard: React.FC<StatCardProps> = ({
  title,
  urduTitle,
  value,
  subtitle,
  icon: Icon,
  trend,
  onClick
}) => {
  return (
    <div
      onClick={onClick}
      className={`bg-white border border-neutral-200 rounded-lg p-5 transition-all duration-150 hover:border-neutral-400 ${
        onClick ? 'cursor-pointer hover:shadow-sm' : ''
      }`}
    >
      <div className="flex items-start justify-between">
        <div>
          <p className="text-xs font-medium uppercase tracking-wider text-neutral-500 flex items-center gap-1.5">
            <span>{title}</span>
            {urduTitle && <span className="font-urdu text-neutral-400 text-sm">({urduTitle})</span>}
          </p>
          <h3 className="text-2xl font-bold tracking-tight text-neutral-900 mt-2">
            {value}
          </h3>
        </div>
        <div className="p-2.5 bg-neutral-50 border border-neutral-100 rounded-md text-neutral-800">
          <Icon className="w-5 h-5" strokeWidth={1.75} />
        </div>
      </div>

      {(subtitle || trend) && (
        <div className="mt-3 flex items-center justify-between text-xs text-neutral-500 pt-2 border-t border-neutral-100">
          {subtitle && <span>{subtitle}</span>}
          {trend && (
            <span
              className={`font-medium ${
                trend.isPositive ? 'text-emerald-700' : 'text-neutral-600'
              }`}
            >
              {trend.value}
            </span>
          )}
        </div>
      )}
    </div>
  );
};
